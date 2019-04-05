package com.example.demo.service;


import com.example.demo.commands.MainMenuMessage;

import com.example.demo.commands.SaveQuestionnareMessage;
import com.example.demo.commands.inline.ApproveQuestionnareMessage;
import com.example.demo.commands.inline.SavedQuestionnareMessage;
import com.example.demo.commands.inline.UploadPhotoRequestMessage;
import com.example.demo.commands.inline.CustomErrorMessage;
import com.example.demo.commands.inline.QuestionsMessage;
import com.example.demo.commands.inline.StartQuestionnareMessage;
import com.example.demo.interfaces.ICacheService;
import com.example.demo.interfaces.IQuestionnareService;
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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Артем on 28.03.2019.
 */
@Service
public class QuastionnareService extends BaseService implements IQuestionnareService {
    private final ICacheService cacheService;
    private ConcurrentHashMap<Integer, Questionnare> userOnStage = new ConcurrentHashMap<>();
    private final QuestionnareDaoRepository questionnareDaoRepository;

    @Autowired
    public QuastionnareService(CacheService cacheService, QuestionnareDaoRepository questionnareDaoRepository) {
        this.SERVICE_ID = 2;
        allowableCommands.add("Создать аукцион");
        allowableCallbackQueries.add("/start-questionnare");
        allowableCallbackQueries.add("/save-questionnare");

        this.cacheService = cacheService;
        this.questionnareDaoRepository = questionnareDaoRepository;
    }

    @Override
    public MessageTransportDto operateMessage(Update update) {
        User user = update.getMessage().getFrom();
        if (hasCommand(update.getMessage().getText())) {
            if (cacheService.getServiceByUserIdInCache(user.getId()) == this.SERVICE_ID) {
                return null;
            }
            cacheService.addToCache(this.SERVICE_ID, user.getId());
            //Задать вопрос "Создать заявку"
            return new StartQuestionnareMessage().addBackButton("/back/menu").toMessageTransportDto();
        } else {
            //Путь когда пользователь присылает сообщение ( НЕ КОМАНДА)
            operateQuestionnare(update, userOnStage.get(user.getId()));
        }

        return null;
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        SendMessage sendMessage = null;
        switch (update.getCallbackQuery().getData()) {
        case "/start-questionnare": {
            return askQuestion(update);
        }
        case "/save-questionnare": {
            return saveQuestionnare(update);
        }
        default:
            return null;
        }
    }

    private MessageTransportDto saveQuestionnare(Update update) {
        User user = update.getMessage().getFrom();
        QuestionnareDao questionnareDao = new QuestionnareDao(userOnStage.get(user.getId()));
        //save anketa to DB
        questionnareDaoRepository.saveAndFlush(questionnareDao);
        //clear caches
        userOnStage.remove(update.getMessage().getFrom().getId());
        cacheService.removeFromCache(user.getId());

        return new SavedQuestionnareMessage().toMessageTransportDto();
    }

    //add user to stageCache, if he is in cache delete and add again(clear history)
    private void addUserToStageCache(User user) {
        if (userOnStage.containsKey(user.getId())) {
            userOnStage.remove(user.getId());
        }
        userOnStage.put(user.getId(), new Questionnare(user));
    }


    private MessageTransportDto askQuestion(Update update) {
        User user = update.getMessage().getFrom();
        if (userOnStage.contains(user.getId())) {
            userOnStage.remove(user.getId());
        } else {
            addUserToStageCache(user);
        }

        //message with questions
        return new QuestionsMessage().toMessageTransportDto();
    }


    private MessageTransportDto operateQuestionnare(Update update, Questionnare questionnare) {
        if (!questionnare.isFull()) {
            return buildQuestionnare(update, questionnare);
        } else {
            return new CustomErrorMessage("Анкета уже заполнена").toMessageTransportDto();
        }
    }


    private MessageTransportDto buildQuestionnare(Update update, Questionnare questionnare) {
        MessageTransportDto messageTransportDto = null;
        if (update.getMessage().hasText() && questionnare.getPhotoId() == null) {

            List<String> answers = List.of(update.getMessage().getText().split("\n"));
            //Ошибка если не все ответы(типо)
            if (answers.size() < QuestionsList.questions.size()) {
                return new CustomErrorMessage("Нужно ответить на все вопросы").toMessageTransportDto();
            }

            questionnare.setAnswers(answers);
            this.userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);

            //ask to send photo
            messageTransportDto = new UploadPhotoRequestMessage().toMessageTransportDto();
        } else {

            List<PhotoSize> photoList = update.getMessage().getPhoto();
            if (photoList.size() > 0) {
                PhotoSize photoSize = photoList.get(photoList.size() - 1);
                if (questionnare.getPhotoId() == null && !questionnare.getAnswers().isEmpty()) {
                    questionnare.setPhotoId(photoSize.getFileId());
                }
            }
            //ask if anketa is okay

        }
        this.userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);
        if (questionnare.isFull()) {
            messageTransportDto = new ApproveQuestionnareMessage().addBackButton("/back/rebuild-questionnare")
                                                                  .toMessageTransportDto();
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setCaption(questionnare.getAnswers());
            sendPhoto.setPhoto(questionnare.getPhotoId());

            messageTransportDto.setSendPhoto(sendPhoto);
        }
        return messageTransportDto;
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
