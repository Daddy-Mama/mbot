package com.example.demo.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class Command {
    protected final String name;
    protected String text;
    protected ReplyKeyboardMarkup replyKeyboardMarkup;
    protected SendMessage sendMessage;


    public Command(String name) {
        this.name = name;
        this.replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
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

    public SendMessage execute() {

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        return sendMessage;
    }


}
