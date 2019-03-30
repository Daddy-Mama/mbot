package com.example.demo.service;


import com.example.demo.commands.Commands;
import com.example.demo.interfaces.IBaseService;
import com.example.demo.interfaces.IMainMenuService;
import com.example.demo.interfaces.ITranslatorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TranslatorService implements ITranslatorService {
    private final static Logger logger = LogManager.getLogger();


    private final CacheService cacheService;
    private final Map<Integer, BaseService> baseServiceMap;

//    private enum services {MAIN_MENU}
//
//    ;

    @Autowired
    public TranslatorService(CacheService cacheService,
                             MainMenuService mainMenuService) {
        this.cacheService = cacheService;
        baseServiceMap = new HashMap<>();
        baseServiceMap.put(mainMenuService.getSERVICE_ID(), mainMenuService);
    }

    public SendMessage executeCommand(Update update) {
        User user = update.getMessage().getFrom();
        String command = parseMessage(update.getMessage().getText());
        logger.info("New update received: " + user.getId() + " " + user.getUserName());


        BaseService service = getServiceForUser(user.getId());
        if (service != null) {
            return registeredUserMessage(service, update);
        }
        for (BaseService baseService : baseServiceMap.values()) {
            if (baseService.hasCommand(command)) {
                service = baseService;
                break;
            }
        }
        if (service != null) {
            return commandFromUserMessage(service, update);
        }


        return errorMessage(update);
    }

    private SendMessage commandFromUserMessage(BaseService service, Update update) {
        return service.execute(update);
    }

    private SendMessage registeredUserMessage(BaseService service, Update update) {
        SendMessage answer = service.execute(update);
        return answer;
    }

    private SendMessage errorMessage(Update update) {
        SendMessage answer = new SendMessage();
        answer.setChatId(update.getMessage().getChatId());
        answer.setText("Что-то странное.. Не могу разобраться:( Напиши в Support, пожалуйста, пусть починят меня!");
        return answer;
    }

    private BaseService getServiceForUser(int userId) {
        return baseServiceMap.get(cacheService.getServiceByUserIdInCache(userId));
    }

    private final String parseMessage(String parsingMessage) {
        return parsingMessage.split(" ")[0];
    }
}
