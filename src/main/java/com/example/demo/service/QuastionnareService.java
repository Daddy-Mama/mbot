package com.example.demo.service;

import com.example.demo.commands.MainMenuMessage;
import com.example.demo.commands.QuestionnareStartMessage;
import com.example.demo.commands.QuestionsMessage;
import com.example.demo.commands.UploadPhotoRequestMessage;
import com.example.demo.interfaces.repositories.QuestionnareDaoRepository;
import com.example.demo.model.Questionnare;
import com.example.demo.model.QuestionsList;
import com.example.demo.model.dao.QuestionnareDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

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
    public QuastionnareService(CacheService cacheService,QuestionnareDaoRepository questionnareDaoRepository) {
        allowableCommands.add("Создать аукцион");
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
            buildQuestionnare(update, questionnare);
            return new MainMenuMessage("Теперь ты участник").toSendMessage(update.getMessage().getChatId());
        }
    }


    private SendMessage buildQuestionnare(Update update, Questionnare questionnare) {
        if (update.getMessage().getText() == null || update.getMessage()
                                                           .getText()
                                                           .isEmpty() && questionnare.getPhotoId() == null) {
            //ask him for photo
            List<String> answers = List.of(update.getMessage().getText().split("\n"));
            //check if answers are in correct form
            SendMessage sendMessage = validateAnswers(answers, update);
            if (sendMessage != null) {
                return sendMessage;
            }
            questionnare.setAnswers(answers);
            this.userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);
            return new UploadPhotoRequestMessage().toSendMessage(update.getMessage().getChatId());
        } else if (update.getMessage().getPhoto().size() > 0) {
            List<PhotoSize> photoList = update.getMessage().getPhoto();
            PhotoSize photoSize = photoList.get(photoList.size()-1);
            if (questionnare.getPhotoId()==null){
                questionnare.setPhotoId(photoSize.getFileId());
            }
        }
        this.userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);
        if (questionnare.isFull()){
            QuestionnareDao questionnareDao = new QuestionnareDao(questionnare);
            //save anketa to DB
            questionnareDaoRepository.saveAndFlush(questionnareDao);
            SendMessage sendMessage = new SendMessage();
            //Create class that must be converted to JSON for tranport data to HighLevel
            //there parse this and send.
//            sendMessage.set
        }


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
