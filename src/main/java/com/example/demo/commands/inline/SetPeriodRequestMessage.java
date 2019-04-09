package com.example.demo.commands.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class SetPeriodRequestMessage extends BaseInlineMessage {
    public SetPeriodRequestMessage() {
        this.text = "Сколько дней будет длиться аукцион?";
        setInfo();
    }

    @Override
    protected void setInfo() {
        buttonsList.add(new InlineKeyboardButton().setText("3 Дня").setCallbackData("/set-period/3"));
        buttonsList.add(new InlineKeyboardButton().setText("1 Неделя").setCallbackData("/set-period/7"));
        buttonsList.add(new InlineKeyboardButton().setText("2 Недели").setCallbackData("/set-period/14"));
    }

}
