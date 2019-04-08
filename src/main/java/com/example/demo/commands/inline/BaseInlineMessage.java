package com.example.demo.commands.inline;

import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BaseInlineMessage {

    protected String text;
    protected List<InlineKeyboardButton> buttonsList;
    protected InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

    public BaseInlineMessage() {

        buttonsList = new ArrayList<>();
    }

    protected void setInfo() {}

    ;

    public MessageTransportDto toMessageTransportDto() {
         SendMessage sendMessage = new SendMessage();
        sendMessage.setText(this.text);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (InlineKeyboardButton button : buttonsList) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(button);
            rowsInline.add(rowInline);
        }
        inlineKeyboardMarkup.setKeyboard(rowsInline);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        MessageTransportDto messageTransportDto = new MessageTransportDto();
        messageTransportDto.setSendMessage(sendMessage);
        return messageTransportDto;
    }

    public BaseInlineMessage addBackButton(String link){
        buttonsList.add(new InlineKeyboardButton().setText("Back").setCallbackData(link));
        return this;
    }

}
