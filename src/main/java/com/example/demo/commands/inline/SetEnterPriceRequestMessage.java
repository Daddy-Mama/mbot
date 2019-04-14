package com.example.demo.commands.inline;

import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class SetEnterPriceRequestMessage extends BaseInlineMessage {
    public SetEnterPriceRequestMessage() {
        this.text = "Какая будет цена участия в аукционе";
        setInfo();
    }

    @Override
    protected void setInfo() {
        buttonsList.add(new InlineKeyboardButton().setText("0 грн").setCallbackData("/set-enter-price/0"));
        buttonsList.add(new InlineKeyboardButton().setText("20 грн").setCallbackData("/set-enter-price/20"));
        buttonsList.add(new InlineKeyboardButton().setText("50 грн").setCallbackData("/set-enter-price/50"));
        buttonsList.add(new InlineKeyboardButton().setText("100 грн").setCallbackData("/set-enter-price/100"));
    }

//    @Override
//    public MessageTransportDto toMessageTransportDto() {
//        EditMessageText editMessageText = new EditMessageText();
//        editMessageText.setText(this.text);
//
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        for (InlineKeyboardButton button : buttonsList) {
//            List<InlineKeyboardButton> rowInline = new ArrayList<>();
//            rowInline.add(button);
//            rowsInline.add(rowInline);
//        }
//
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
//
//        MessageTransportDto messageTransportDto = new MessageTransportDto();
//        messageTransportDto.setEditMessageText(editMessageText);
//        return messageTransportDto;
//
//    }
}
