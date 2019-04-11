package com.example.demo.service;


import com.example.demo.commands.inline.ApproveQuestionnareMessage;
import com.example.demo.commands.inline.QuestionnareSavedMessage;
import com.example.demo.commands.inline.SetEnterPriceRequestMessage;
import com.example.demo.commands.inline.SetPeriodRequestMessage;
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
import com.example.demo.model.dto.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        allowableCallbackQueries.add("/set-period");
        allowableCallbackQueries.add("/set-enter-price");
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
            return setAnswer(update, user);
        }
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        List<String> request = Arrays.asList(update.getCallbackQuery().getData().split("/"));
        request = request.stream().filter(x -> !x.equals("")).collect(Collectors.toList());
        switch ("/" + request.get(0)) {
        case "/start-questionnare": {
            return askQuestion(update);
        }
        case "/save-questionnare": {
            return saveQuestionnare(update, update.getCallbackQuery().getFrom());
        }
        case "/set-period": {
            return setPeriod(update, update.getCallbackQuery().getFrom(), Integer.parseInt(request.get(1)));
        }
        case "/set-enter-price": {
            return setEnterPrice(update, update.getCallbackQuery().getFrom(), Integer.parseInt(request.get(1)));
        }
        default:
            return null;
        }
    }

    private MessageTransportDto setEnterPrice(Update update, User user, int enterPrice) {
        MessageTransportDto messageTransportDto = null;
        if (userOnStage.containsKey(user.getId())) {
            Questionnare questionnare = userOnStage.get(user.getId());
            if (questionnare.getEnterPrice() == null) {
                questionnare.setEnterPrice(enterPrice);
                 return askApprove(questionnare);
            } else {
                return new CustomErrorMessage("Ошибка: цена участия уже установлена").toMessageTransportDto();
            }
        }
        return null;
    }

    private MessageTransportDto setPeriod(Update update, User user, int period) {
        MessageTransportDto messageTransportDto = null;
        if (userOnStage.containsKey(user.getId())) {
            Questionnare questionnare = userOnStage.get(user.getId());
            if (questionnare.getPeriod() == null) {
                questionnare.setPeriod(LocalDate.now().plusDays(period));
                 return new SetEnterPriceRequestMessage().toMessageTransportDto();
            } else {
                return new CustomErrorMessage("Ошибка: период уже установлен").toMessageTransportDto();
            }
        }
        return null;
    }

    @Override
    public MessageTransportDto operatePhoto(Update update) {
        User user = update.getMessage().getFrom();
        if (update.getMessage().hasText()) {
            return new CustomErrorMessage("Не вводи текст вместе с фото").toMessageTransportDto();
        }
        return setPhoto(update, user);

    }

    private MessageTransportDto saveQuestionnare(Update update, User user) {
        //save anketa to DB
        MessageTransportDto messageTransportDto = databaseService.saveQuestionnare(userOnStage.get(user.getId()));
        //clear caches
        userOnStage.remove(user.getId());
        cacheService.removeFromCache(user.getId());

        return messageTransportDto;
    }


//    private MessageTransportDto buildQuestionnare(Update update, User user) {
//        MessageTransportDto messageTransportDto = null;
//        if (userOnStage.containsKey(user.getId())) {
//            Questionnare questionnare = userOnStage.get(user.getId());
//            switch (questionnare.getStatus()) {
//            case 0: {
//                messageTransportDto = setAnswer(questionnare, update);
//                if (messageTransportDto != null) {
//                    return messageTransportDto;
//                }
//                //Return request to upload photo
//                return new UploadPhotoRequestMessage().toMessageTransportDto();
//            }
//            case 1: {
//                messageTransportDto = setPhoto(questionnare, update);
//                if (messageTransportDto != null) {
//                    return messageTransportDto;
//                }
//                return new SetPeriodRequestMessage().toMessageTransportDto();
//                //                return askApprove(questionnare);
//            }
////            case 2: {
////                messageTransportDto = setPeriod(questionnare, update);
////            }
//            }
//        }
//        return null;
//    }


    private MessageTransportDto askApprove(Questionnare questionnare) {
        MessageTransportDto messageTransportDto = null;
        if (questionnare.isFull()) {
            messageTransportDto = new ApproveQuestionnareMessage().addBackButton("/back/rebuild-questionnare")
                                                                  .toMessageTransportDto(questionnare);
        }
        return messageTransportDto;
    }

    private MessageTransportDto setPhoto(Update update, User user) {

        if (userOnStage.containsKey(user.getId()) && update.getMessage().hasPhoto()) {
            Questionnare questionnare = userOnStage.get(user.getId());
            if (questionnare.getPhotoId() == null) {
                List<PhotoSize> photoList = update.getMessage().getPhoto();

                PhotoSize photoSize = photoList.get(photoList.size() - 1);
//                    if (questionnare.getPhotoId() == null && !questionnare.getAnswers().isEmpty()) {
                questionnare.setPhotoId(photoSize.getFileId());
                 userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);
                return new SetPeriodRequestMessage().toMessageTransportDto();
//                    }
            } else {
                return new CustomErrorMessage("Ошибка: фото анкеты уже установлено").toMessageTransportDto();
            }
        }
        return null;

    }

    private MessageTransportDto setAnswer(Update update, User user) {
        if (userOnStage.containsKey(user.getId()) && update.getMessage().hasText()) {
            Questionnare questionnare = userOnStage.get(user.getId());
            if (questionnare.getAnswers() == null) {
                List<String> answers = List.of(update.getMessage().getText().split("\n"));
                //Ошибка если не все ответы(типо)
                if (answers.size() < QuestionsList.questions.size()) {
                    return new CustomErrorMessage("Нужно ответить на все вопросы").toMessageTransportDto();
                }

                questionnare.setAnswers(answers);

                userOnStage.replace(update.getMessage().getFrom().getId(), questionnare);

                //ask to send photo
                return new UploadPhotoRequestMessage().toMessageTransportDto();
            } else {
                return new CustomErrorMessage("Ошибка: описание анкеты уже установлено").toMessageTransportDto();
            }
        }
//        return new CustomErrorMessage("Ошибка: пустое сообщение или анкета не активна").toMessageTransportDto();
        return null;
    }

    private MessageTransportDto askQuestion(Update update) {
        User user = update.getCallbackQuery().getFrom();
        if (userOnStage.containsKey(user.getId())) {
            userOnStage.remove(user.getId());
        } else {
            Questionnare questionnare = new Questionnare(user);
            userOnStage.put(user.getId(), questionnare);
        }
        //message with questions
        return new QuestionsMessage().toMessageTransportDto();
    }


}
