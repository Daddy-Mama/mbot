package com.example.demo.service;

import com.example.demo.commands.inline.CustomErrorMessage;
import com.example.demo.commands.inline.StartQuestionnareMessage;
import com.example.demo.commands.inline.StartSearchRequestMessage;
import com.example.demo.commands.inline.UploadPhotoRequestMessage;
import com.example.demo.interfaces.ICacheService;
import com.example.demo.interfaces.IDatabaseService;
import com.example.demo.interfaces.IPaymentService;
import com.example.demo.interfaces.ISearchService;
import com.example.demo.model.Questionnare;
import com.example.demo.model.QuestionsList;
import com.example.demo.model.dto.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService extends BaseService implements ISearchService {
    private final ICacheService cacheService;
    @Autowired
    private IDatabaseService databaseService;
    @Autowired
    private IPaymentService paymentService;

    @Autowired
    public SearchService(CacheService cacheService) {
        this.SERVICE_ID = 3;
        allowableCommands.add("Поиск аукциона");
        allowableCallbackQueries.add("/buy-date");
        this.cacheService = cacheService;
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
            return new StartSearchRequestMessage().addBackButton("/back/menu").toMessageTransportDto();
        } else {
            //Путь когда пользователь присылает сообщение ( НЕ КОМАНДА)
            return setAnswer(update, user);
        }
    }

    private MessageTransportDto setAnswer(Update update, User user) {
        if (update.getMessage().hasText()) {
            try {
                if (!update.getMessage().getText().startsWith("@")) {
                    return new CustomErrorMessage("Никнейм начинается с '@'").toMessageTransportDto();
                }
                String userName = parseCommand(update.getMessage().getText());
                return databaseService.findByUserName(userName);
            } catch (ParseException e) {
                return new CustomErrorMessage("Введи только: @никнейм_пользователя").toMessageTransportDto();
            }
        }
        return null;
    }


    private String parseCommand(String command) throws ParseException {
        List<String> messageList = Arrays.asList(command.split("@"));
        messageList = messageList.stream().filter(x -> !x.equals("")).collect(Collectors.toList());
        if (messageList.size() > 1) {
            throw new ParseException("", 0);
        } else {
            return messageList.get(0);
        }
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        List<String> request = Arrays.asList(update.getCallbackQuery().getData().split("/"));
        request = request.stream().filter(x -> !x.equals("")).collect(Collectors.toList());
        switch ("/" + request.get(0)) {
            case "/buy-date": {
               return paymentService.buyDate(request.get(1));
            }
        }
        return new CustomErrorMessage("Что-то пошло не так при поиске").toMessageTransportDto();
    }

    @Override
    public MessageTransportDto operatePhoto(Update update) {
        return null;
    }

}
