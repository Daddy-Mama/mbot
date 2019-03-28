package com.example.demo.service;

import com.example.demo.commands.Commands;
import com.example.demo.interfaces.IMainMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.commands.Commands.START;

@Service
public class MainMenuService extends BaseService implements IMainMenuService {
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    @Autowired
    public MainMenuService() {
        allowableCommands = new ArrayList<>();
        allowableCommands.add(START);
    }


    public void parseMessage(Update update) {
        String message = update.getMessage().getText();

    }

    @Override
    public SendMessage executeCommand(String command) {
        assert (hasCommand(command));
        super.executeCommand(command);
    }


}
