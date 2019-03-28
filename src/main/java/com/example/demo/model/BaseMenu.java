package com.example.demo.model;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BaseMenu {
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    protected final List<String> button = List.of("Мой профиль=)", "Найти человека", "Открыть собственный аукцион",
                                                  "FAQ",
                                                  "Вопросы и предложения", "Вернуться в главное меню");

    public BaseMenu() {

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        replyKeyboardMarkup.setKeyboard(
                createKeyboard(button.stream()
                                     .filter(x -> !x.equals("Вернуться в главное меню"))
                                     .collect(Collectors.toList())));
    }

    protected ArrayList<KeyboardRow> createKeyboard(List<String> list) {

        ArrayList<KeyboardRow> keyboard = new ArrayList<>(3);

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(list.get(0));

        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(list.get(1));
        keyboardSecondRow.add(list.get(2));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(list.get(3));
        keyboardThirdRow.add(list.get(4));

        return keyboard;
    }
}
