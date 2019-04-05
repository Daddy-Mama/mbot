package com.example.demo.commands.inline;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class StartQuestionnareMessage extends BaseInlineMessage {
    public StartQuestionnareMessage() {
        this.text = "Хотите заполнить анкету на участие?";
    }

    @Override
    protected void setInfo() {
        buttonsList.add(new InlineKeyboardButton().setText("Заполнить").setCallbackData("/start-questionnare"));
    }

}
