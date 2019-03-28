package com.example.demo.model;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenuModel extends BaseMenu {
    public MainMenuModel() {
        createKeyboard(button.stream().filter(x->x.equals("Вернуться в главное меню")).collect(Collectors.toList()));
    }

    @Override
    protected ArrayList<KeyboardRow> createKeyboard(List<String> list) {
        return super.createKeyboard(list);
    }
}
