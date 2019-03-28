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
    private final List<IBaseService> listOfServices;

    @Autowired
    public TranslatorService(IMainMenuService mainMenuService) {
        this.listOfServices = new ArrayList<>();
        this.listOfServices.add(mainMenuService);
    }

    public SendMessage executeCommand(Update update) {
        User user = update.getMessage().getFrom();
        logger.info("New update received: " + user.getId() + " " + user.getUserName());
        String command = parseMessage(update.getMessage().getText());

        List<SendMessage> sendMessages = listOfServices.stream()
                                                       .map(x -> x.executeCommand(command))
                                                       .collect(Collectors.toList());

        return sendMessages.get(0);
    }

    private final String parseMessage(String parsingMessage) {

        return parsingMessage.split(" ")[0];

    }
}
