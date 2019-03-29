package com.example.demo.service;


import com.example.demo.commands.Command;
import com.example.demo.commands.Commands;
import com.example.demo.interfaces.IBaseService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseService {

    protected List<String> allowableCommands = new ArrayList<>();
    private List<Integer> chatIdInServiceList = new ArrayList<>();

    protected ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public BaseService() {
        setReplyKeyboardMarkup();
    }

    public boolean hasUserId(int id) {
        return chatIdInServiceList.contains(id);
    }

    public boolean hasCommand(String command) {
        return this.allowableCommands
                .stream()
                .anyMatch(x -> x.toLowerCase().contains(command));
    }
    protected void setReplyKeyboardMarkup(){}

    abstract SendMessage execute(Update update);
}
