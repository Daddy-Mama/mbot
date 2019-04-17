package com.example.demo.model.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class NotificationMessage {
    private SendMessage sendMessage;

    public NotificationMessage(String user, Long chatId) {
        this.sendMessage = new SendMessage();
        this.sendMessage.setChatId(chatId);
        this.sendMessage.setText("@"+user + " оплатил встречу с тобой! Напиши ему когда тебе удобно)\n ");
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }
}
