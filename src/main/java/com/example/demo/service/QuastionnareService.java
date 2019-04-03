package com.example.demo.service;

import com.example.demo.commands.CustomErrorMessage;
import com.example.demo.commands.MainMenuMessage;
import com.example.demo.commands.QuestionnareStartMessage;
import com.example.demo.commands.QuestionsMessage;
import com.example.demo.commands.SaveQuestionnareMessage;
import com.example.demo.commands.UploadPhotoRequestMessage;
import com.example.demo.interfaces.repositories.QuestionnareDaoRepository;
import com.example.demo.model.Questionnare;
import com.example.demo.model.QuestionsList;
import com.example.demo.model.dao.QuestionnareDao;
import com.example.demo.model.dto.MessageTransportDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Артем on 28.03.2019.
 */
@Service
public class QuastionnareService extends BaseService {
    private final CacheService cacheService;
    private ConcurrentHashMap<Integer, Questionnare> userOnStage = new ConcurrentHashMap<>();
    private final QuestionnareDaoRepository questionnareDaoRepository;

    @Autowired
    public QuastionnareService(CacheService cacheService, QuestionnareDaoRepository questionnareDaoRepository) {
        allowableCommands.add("Создать аукцион");
        allowableCommands.add("Сохранить анкету!");
        this.cacheService = cacheService;
        this.SERVICE_ID = 2;
        this.questionnareDaoRepository = questionnareDaoRepository;
    }

    @Override
    public SendMessage execute(Update update, boolean isCommand) {
        User user = update.getMessage().getFrom();
        if (isCommand) {
            cacheService.addToCache(this.SERVICE_ID, user.getId());
            addUserToStageCache(user);
            return letsStartMessage(update);
        } else {
            return askQuestion(update);
        }
//        if (null == cacheService.getServiceByUserIdInCache(user.getId())) {
//            cacheService.addToCache(this.SERVICE_ID, user.getId());
//            return new QuestionnareStartMessage().toSendMessage(update.getMessage().getChatId());
//        }

    }

    //add user to stageCache, if he is in cache delete and add again(clear history)
    private void addUserToStageCache(User user) {
        if (userOnStage.containsKey(user.getId())) {
            userOnStage.remove(user.getId());
        }
        userOnStage.put(user.getId(), new Questionnare(user));
    }

    private SendMessage letsStartMessage(Update update) {
        return new QuestionnareStartMessage().toSendMessage(update.getMessage().getChatId());
    }

    private SendMessage askQuestion(Update update) {
        User user = update.getMessage().getFrom();
        Questionnare questionnare = userOnStage.get(user.getId());
        if (!questionnare.isInProgress()) {
            questionnare.setInProgress(true);
            return new QuestionsMessage().toSendMessage(update.getMessage().getChatId());
        } else {
            return buildQuestionnare(update, questionnare);
//            return new MainMenuMessage("Теперь ты участник").toSendMessage(update.getMessage().getChatId());
        }
    }


    private SendMessage buildQuestionnare(Update update, Questionnare questionnare) {
        if (!questionnare.isFull()) {
            if (update.getMessage().getText() != null
                    && questionnare.getPhotoId() == null) {
                List<String> answers = List.of(update.getMessage().getText().split("\n"));
                //check if answers are in correct form
                SendMessage sendMessage = validateAnswers(answers, update);
                if (sendMessage != null) {
                    return sendMessage;
                }
                questionnare.setAnswers(answers);
                this.userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);

                //ask to send photo
                return new UploadPhotoRequestMessage().toSendMessage(update.getMessage().getChatId());
            } else {

                List<PhotoSize> photoList = update.getMessage().getPhoto();
                if (photoList.size() > 0) {
                    PhotoSize photoSize = photoList.get(photoList.size() - 1);
                    if (questionnare.getPhotoId() == null) {
                        questionnare.setPhotoId(photoSize.getFileId());
                    }
                }
            }
            this.userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);

        }
        if (questionnare.isFull()) {
 //save to db
            if (update.getMessage().getText() != null
                    && allowableCommands.contains(update.getMessage().getText())) {

                QuestionnareDao questionnareDao = new QuestionnareDao(questionnare);
                //save anketa to DB
                questionnareDaoRepository.saveAndFlush(questionnareDao);
                userOnStage.remove(update.getMessage().getFrom().getId());

                return new MainMenuMessage("Круто! Теперь ты учавствуешь!").toSendMessage(update.getMessage().getChatId());

            } else {

                //Ask is anketa is OK
                MessageTransportDto messageTransportDto = new MessageTransportDto();
                messageTransportDto.setPhotoId(questionnare.getPhotoId());
                messageTransportDto.setText("Твоя анкета: " + "\n" + questionnare.getAnswers());
                String json = "";
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                try {
                    json = ow.writeValueAsString(messageTransportDto);
                    return new SaveQuestionnareMessage(json).toSendMessage(
                            update.getMessage().getChatId());


                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }


        } else {
            return new CustomErrorMessage("Нужно правильно заполнить форму=) Попробуй сначала")
                    .toSendMessage(update.getMessage().getChatId());
        }

        return null;
    }

    private SendMessage validateAnswers(List<String> answers, Update update) {
//        Pattern p = Pattern.compile("^\\d{1,2}[.] [a-zA-Z]+");
//        Optional<String> badLine = answers.stream().filter(x -> !p.matcher(x.trim()).find()).findAny();
//        if (badLine.isPresent()) {
//            SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), "Неправильная строка: " + badLine.get());
//            return sendMessage;
//        }
        if (answers.size() != QuestionsList.questions.size()) {
            SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(),
                                                      "Ответь на все вопросы одной строкой!");
            return sendMessage;
        }
        return null;
    }
}
