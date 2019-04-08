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
import com.example.demo.interfaces.IDatabaseService;
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
    private final IDatabaseService databaseService;

    @Autowired
    public QuastionnareService(DatabaseService databaseService,
                               CacheService cacheService,
                               QuestionnareDaoRepository questionnareDaoRepository) {
        this.SERVICE_ID = 2;
        allowableCommands.add("Создать аукцион");
        allowableCallbackQueries.add("/start-questionnare");
        allowableCallbackQueries.add("/save-questionnare");

        this.cacheService = cacheService;
        this.questionnareDaoRepository = questionnareDaoRepository;
        this.databaseService = databaseService;
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
            return buildQuestionnare(update,user);
        }

    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        SendMessage sendMessage = null;
//        User user = update.getMessage().getFrom();
        switch (update.getCallbackQuery().getData()) {
        case "/start-questionnare": {
            return askQuestion(update);
        }
        case "/save-questionnare": {

            return saveQuestionnare(update, update.getCallbackQuery().getFrom());
        }
        default:
            return null;
        }
    }

    @Override
    public MessageTransportDto operatePhoto(Update update) {
        User user = update.getMessage().getFrom();
        if (update.getMessage().hasText()) {
            return new CustomErrorMessage("Не вводи текст вместе с фото").toMessageTransportDto();
        }
        return buildQuestionnare(update, user);

    }

    private MessageTransportDto saveQuestionnare(Update update,User user) {
        //save anketa to DB
        databaseService.saveQuestionnare(userOnStage.get(user.getId()));
        //clear caches
        userOnStage.remove(user.getId());
        cacheService.removeFromCache(user.getId());

        return new SavedQuestionnareMessage().toMessageTransportDto();
    }

    //add user to stageCache, if he is in cache delete and add again(clear history)


//    private MessageTransportDto operateQuestionnare(Update update, Questionnare questionnare) {
//        if (questionnare == null) {
//            return new CustomErrorMessage("Ошибка сервиса создания анкеты").toMessageTransportDto();
//        }
//        if (!questionnare.isFull()) {
//            return buildQuestionnare(update, questionnare);
//        } else {
//            return new CustomErrorMessage("Анкета уже заполнена").toMessageTransportDto();
//        }
//    }
//    private MessageTransportDto setInfoFromMessage(Update update,
//                                                   Questionnare questionnare,
//                                                   MessageTransportDto messageTransportDto) {
//        if (update.getMessage().hasText() && questionnare.getPhotoId() == null) {
//            List<String> answers = List.of(update.getMessage().getText().split("\n"));
//            //Ошибка если не все ответы(типо)
//            if (answers.size() < QuestionsList.questions.size()) {
//                return new CustomErrorMessage("Нужно ответить на все вопросы").toMessageTransportDto();
//            }
//
//            questionnare.setAnswers(answers);
//            //ask to send photo
//            messageTransportDto = new UploadPhotoRequestMessage().toMessageTransportDto();
//        }
//        if (update.getMessage().getPhoto().size() > 0) {
//            List<PhotoSize> photoList = update.getMessage().getPhoto();
//            if (photoList.size() > 0) {
//                PhotoSize photoSize = photoList.get(photoList.size() - 1);
//                if (questionnare.getPhotoId() == null && !questionnare.getAnswers().isEmpty()) {
//                    questionnare.setPhotoId(photoSize.getFileId());
//                }
//            }
//        }
//        return null;
//    }

//    private MessageTransportDto buildQuestionnare(Update update, Questionnare questionnare) {
//        MessageTransportDto messageTransportDto = null;
//        if (update.hasMessage()) {
//            messageTransportDto = setInfoFromMessage(update, questionnare, messageTransportDto);
//
//            if (messageTransportDto != null) {
//                return messageTransportDto;
//            }
//        }
//
//        this.userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);
//        if (questionnare.isFull()) {
//            messageTransportDto = new ApproveQuestionnareMessage().addBackButton("/back/rebuild-questionnare")
//                                                                  .toMessageTransportDto();
//            SendPhoto sendPhoto = new SendPhoto();
//            sendPhoto.setCaption(questionnare.getAnswers());
//            sendPhoto.setPhoto(questionnare.getPhotoId());
//
//            messageTransportDto.setSendPhoto(sendPhoto);
//        }
//        return messageTransportDto;
//    }

    private MessageTransportDto buildQuestionnare(Update update, User user) {
        MessageTransportDto messageTransportDto = null;
        if (userOnStage.containsKey(user.getId())) {
            Questionnare questionnare = userOnStage.get(user.getId());
            switch (questionnare.getStatus()) {
            case 0: {
                messageTransportDto = setAnswer(questionnare, update);
                if (messageTransportDto != null) {
                    return messageTransportDto;
                }
                //Return request to upload photo
                return new UploadPhotoRequestMessage().toMessageTransportDto();
            }
            case 1: {
                messageTransportDto = setPhoto(questionnare, update);
                if (messageTransportDto != null) {
                    return messageTransportDto;
                }
                return askApprove(questionnare);
            }
            }
        }
        return null;
    }


    private MessageTransportDto askApprove(Questionnare questionnare) {
        MessageTransportDto messageTransportDto = null;
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

    private MessageTransportDto setPhoto(Questionnare questionnare, Update update) {
        if (update.getMessage().hasPhoto()) {
            List<PhotoSize> photoList = update.getMessage().getPhoto();
            if (photoList.size() > 0) {
                PhotoSize photoSize = photoList.get(photoList.size() - 1);
                if (questionnare.getPhotoId() == null && !questionnare.getAnswers().isEmpty()) {
                    questionnare.setPhotoId(photoSize.getFileId());
                    questionnare.setStatus(2);
                    userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);
                }
            }
        } else {
            return new CustomErrorMessage("Здесь должно было быть фото").toMessageTransportDto();
        }
        return null;
    }

    private MessageTransportDto setAnswer(Questionnare questionnare, Update update) {
        if (update.getMessage().hasText()) {
            List<String> answers = List.of(update.getMessage().getText().split("\n"));
            //Ошибка если не все ответы(типо)
            if (answers.size() < QuestionsList.questions.size()) {
                return new CustomErrorMessage("Нужно ответить на все вопросы").toMessageTransportDto();
            }

            questionnare.setAnswers(answers);
            questionnare.setStatus(1);

            userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);
            //ask to send photo
        } else {
            return new CustomErrorMessage("Ошибка обработки ответов").toMessageTransportDto();
        }
        return null;
    }

    private MessageTransportDto askQuestion(Update update) {
        User user = update.getCallbackQuery().getFrom();
        if (userOnStage.containsKey(user.getId())) {
            userOnStage.remove(user.getId());
        } else {
            Questionnare questionnare = new Questionnare(user);
            questionnare.setStatus(0);
            userOnStage.put(user.getId(), questionnare);
        }
        //message with questions
        return new QuestionsMessage().toMessageTransportDto();
    }


}
