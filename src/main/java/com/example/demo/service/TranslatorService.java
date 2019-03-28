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
import java.util.stream.Collectors;

@Component
public class TranslatorService implements ITranslatorService {
    private final static Logger logger = LogManager.getLogger();
    //    private final List<T extends BaseService> listOfServices;
    private final MainMenuService mainMenuService;

    @Autowired
    public TranslatorService(MainMenuService mainMenuService) {
//        this.listOfServices = new ArrayList<>();
//        this.listOfServices.add(mainMenuService);
        this.mainMenuService = mainMenuService;
    }

    public SendMessage executeCommand(Update update) {
        User user = update.getMessage().getFrom();
        String command = parseMessage(update.getMessage().getText());
        logger.info("New update received: " + user.getId() + " " + user.getUserName());
        //Check chatId in every cache and decide which service work with this
        if (!mainMenuService.hasUserId(user.getId()) && !mainMenuService.hasCommand(command)){
            return  mainMenuService.execute(update);
        }
         return null;
    }

    private final String parseMessage(String parsingMessage) {

        return parsingMessage.split(" ")[0];

    }
}
