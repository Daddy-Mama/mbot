package com.example.demo.commands.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
}
