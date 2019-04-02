package com.example.demo.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Артем on 30.03.2019.
 */
public class QuestionnareStartMessage extends BaseMessage{

    public QuestionnareStartMessage() {
       setInfo();
    }

    @Override
    protected void setInfo() {
        this.text = "Ну что, заполнишь анкету на участие?";
        this.buttonsList = new ArrayList<>(List.of("Заполнить анкету"));
        addMainMenuButton();

//        replyKeyboardMarkup.setSelective(true);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(false);
        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRowRow = new KeyboardRow();
        keyboardFirstRowRow.add(new KeyboardButton(buttonsList.get(0)));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton(buttonsList.get(1)));

        keyboard.add(keyboardFirstRowRow);
        keyboard.add(keyboardSecondRow);


        replyKeyboardMarkup.setKeyboard(keyboard);

    }

}
