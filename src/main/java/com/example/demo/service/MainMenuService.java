package com.example.demo.service;

import com.example.demo.commands.MainMenuMessage;
import com.example.demo.interfaces.IMainMenuService;
import com.example.demo.model.dto.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MainMenuService extends BaseService implements IMainMenuService {

    @Autowired
    public MainMenuService() {
        this.SERVICE_ID = 1;

        allowableCommands.add("Меню");
    }


    @Override
    public MessageTransportDto operateMessage(Update update) {
        return showMenu(update);
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        return null;
    }

    private MessageTransportDto showMenu(Update update) {

        return new MainMenuMessage().toMessageTransportDto(update.getMessage().getChatId());
    }

}
