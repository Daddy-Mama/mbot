package com.example.demo.service;


import com.example.demo.commands.Commands;
import com.example.demo.commands.MainMenuMessage;
import com.example.demo.commands.inline.CustomErrorMessage;
import com.example.demo.interfaces.IBaseService;
import com.example.demo.interfaces.ICacheService;
import com.example.demo.interfaces.IMainMenuService;
import com.example.demo.interfaces.IQuestionnareService;
import com.example.demo.interfaces.ITranslatorService;
import com.example.demo.model.dto.MessageTransportDto;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TranslatorService implements ITranslatorService {
    private final static Logger logger = LogManager.getLogger();

    private final ICacheService cacheService;
    private final IMainMenuService menuService;
    private final Map<Integer, IBaseService> baseServiceMap;


    @Autowired
    public TranslatorService(CacheService cacheService,
                             MainMenuService mainMenuService,
                             QuastionnareService quastionnareService) {
        this.menuService = mainMenuService;
        this.cacheService = cacheService;
        baseServiceMap = new HashMap<>();
        baseServiceMap.put(menuService.getSERVICE_ID(), menuService);
        baseServiceMap.put(quastionnareService.getSERVICE_ID(), quastionnareService);
    }


    public MessageTransportDto operateMessage(Update update) {
        User user = update.getMessage().getFrom();
        String command = parseMessage(update.getMessage().getText());
        logger.info("New update received: " + user.getId() + " " + user.getUserName());

        IBaseService service = getServiceForUser(user.getId());

        //user is in cache
        //it means that received message but NOT COMMAND from user
        if (service != null) {
            return registeredUserMessage(service, update);
        }
        //check if this IS COMMAND
        if (command != null) {
            for (IBaseService baseService : baseServiceMap.values()) {
                if (baseService.hasCommand(command)) {
                    return baseService.operateMessage(update);

                }
            }
        }
//        //user sent command and he does't exist in cache
//        if (service != null) {
//            return commandFromUserMessage(service, update);
//        }
        return errorMessage(update);
    }


    public MessageTransportDto operateCallbackQuery(Update update) {
        String call_data = update.getCallbackQuery().getData();
        long message_id = update.getCallbackQuery().getMessage().getMessageId();
        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        MessageTransportDto messageTransportDto = null;
        IBaseService service;

        if (call_data != null) {
            for (IBaseService baseService : baseServiceMap.values()) {
                if (baseService.hasCallbackQuery(call_data)) {
                    service = baseService;
                    messageTransportDto = service.operateCallbackQuery(update);
                    break;
                }
            }
        }
        return messageTransportDto;
    }

    @Override
    public MessageTransportDto operatePhoto(Update update) {
        User user = update.getMessage().getFrom();
        IBaseService service = getServiceForUser(user.getId());
        if (service!=null){
            return service.operatePhoto(update);
        }
        else {
            return new CustomErrorMessage("Ошибка обработки фотографии").toMessageTransportDto();
        }
    }

    //    public synchronized SendMessage executeCommand(Update update) {
//        User user = update.getMessage().getFrom();
//        String command = parseMessage(update.getMessage().getText());
//        logger.info("New update received: " + user.getId() + " " + user.getUserName());
//        BaseService service = getServiceForUser(user.getId());
//        //user is in cache
//        if (service != null) {
//            return registeredUserMessage(service, update);
//        }
//        if (command != null) {
//            for (BaseService baseService : baseServiceMap.values()) {
//                if (baseService.hasCommand(command)) {
//                    service = baseService;
//                    break;
//                }
//            }
//        }
//        //user sent command and he does't exist in cache
//        if (service != null) {
//            return commandFromUserMessage(service, update);
//        }
//
//
//        return errorMessage(update);
//    }

    private MessageTransportDto commandFromUserMessage(IBaseService service, Update update) {
        return service.operateMessage(update);
    }

    private MessageTransportDto registeredUserMessage(IBaseService service, Update update) {
        MessageTransportDto answer = service.operateMessage(update);
        return answer;
    }

    private MessageTransportDto errorMessage(Update update) {

        return new MainMenuMessage(
                "Что-то странное.. Может ты предыдущую сессию не закончил:( Верну тебя в меню").toMessageTransportDto(
                update.getMessage().getChatId());
    }

    private IBaseService getServiceForUser(int userId) {
        return baseServiceMap.get(cacheService.getServiceByUserIdInCache(userId));
    }

    private final String parseMessage(String parsingMessage) {
        if (parsingMessage != null) {
            return parsingMessage.toLowerCase();
        } else {
            return null;
        }

    }
}
