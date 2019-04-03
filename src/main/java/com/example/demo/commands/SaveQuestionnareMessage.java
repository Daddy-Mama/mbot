package com.example.demo.commands;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SaveQuestionnareMessage extends BaseMessage {

    public SaveQuestionnareMessage(String text) {
        this.text = text;
        setInfo();
        addMainMenuButton();
    }

    @Override
    protected void setInfo() {
        buttonsList.add("Сохранить анкету!");
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        for (String but : buttonsList) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(but));
            keyboard.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
}
