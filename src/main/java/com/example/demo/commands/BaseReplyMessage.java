package com.example.demo.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class BaseReplyMessage {
    protected final String name;
    protected String text;
    protected ReplyKeyboardMarkup replyKeyboardMarkup;
    protected SendMessage sendMessage;


    public BaseReplyMessage(String name) {
        this.name = name;
        this.replyKeyboardMarkup = new ReplyKeyboardMarkup();
        this.sendMessage = new SendMessage();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        return replyKeyboardMarkup;
    }

    public void setReplyKeyboardMarkup(ReplyKeyboardMarkup replyKeyboardMarkup) {
        this.replyKeyboardMarkup = replyKeyboardMarkup;
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public String getName() {
        return name;
    }

    public SendMessage execute(){
        //Execute this command and return result SendMessage;
    }



}
