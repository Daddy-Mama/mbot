package com.example.demo.service;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseService {
    protected int SERVICE_ID;
    protected List<String> allowableCommands = new ArrayList<>();
//    private List<Integer> chatIdInServiceList = new ArrayList<>();
//
//    protected ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public int getSERVICE_ID() {
        return SERVICE_ID;
    }

    public BaseService() {
//        setReplyKeyboardMarkup();
    }

//    public boolean hasUserId(int id) {
//        return chatIdInServiceList.contains(id);
//    }

    public boolean hasCommand(String command) {
        return this.allowableCommands
                .stream()
                .anyMatch(x -> x.toLowerCase().contains(command));
    }
//    protected void setReplyKeyboardMarkup(){}

    abstract SendMessage execute(Update update);
}
