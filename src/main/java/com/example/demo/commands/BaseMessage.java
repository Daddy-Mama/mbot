package com.example.demo.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

/**
 * Created by Артем on 30.03.2019.
 */
public abstract class BaseMessage {
    protected String text;
    protected List<String> buttonsList;
    protected ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public BaseMessage() {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
    }

    abstract void setInfo();

    public SendMessage toSendMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(this.text);
        sendMessage.setReplyMarkup(this.replyKeyboardMarkup);
        return sendMessage;
    }

    protected void addMainMenuButton() {
        buttonsList.add("Вернуться в главное меню!");
    }
}
