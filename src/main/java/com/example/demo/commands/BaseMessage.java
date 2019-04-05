package com.example.demo.commands;

import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Артем on 30.03.2019.
 */
public class BaseMessage {
    protected String text;
    protected List<String> buttonsList;
    protected ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public BaseMessage() {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        buttonsList = new ArrayList<>();
    }

    protected void setInfo(){};

    public MessageTransportDto toMessageTransportDto(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(this.text);
        sendMessage.setReplyMarkup(this.replyKeyboardMarkup);

        MessageTransportDto messageTransportDto = new MessageTransportDto();
        messageTransportDto.setSendMessage(sendMessage);
        return messageTransportDto;
    }

    protected void addMainMenuButton() {
        buttonsList.add("Вернуться в главное меню!");
    }
}
