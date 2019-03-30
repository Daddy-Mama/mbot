package com.example.demo.commands;

import com.example.demo.model.Questions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Артем on 30.03.2019.
 */
public class QuestionsMessage extends BaseMessage {
    public QuestionsMessage() {
        setInfo();
        addMainMenuButton();
    }

    @Override
    protected void setInfo() {
        this.text = Questions.questions.stream().collect(Collectors.joining("\n"));
        buttonsList.add("Ok");
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(buttonsList.get(0)));
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardSecondRow.add(new KeyboardButton(buttonsList.get(1)));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

    }
}
