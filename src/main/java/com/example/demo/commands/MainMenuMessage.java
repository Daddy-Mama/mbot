package com.example.demo.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Артем on 30.03.2019.
 */
public class MainMenuMessage extends BaseMessage{
//    private String text;
//    private  List<String> buttonsList;
//    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public MainMenuMessage() {
        this.text = "Вот тебе менюшка";
        setInfo();
    }

    public MainMenuMessage(String text) {
        this.text = text;
        setInfo();
    }

//    public SendMessage toSendMessage(Long chatId) {
//
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(this.text);
//        sendMessage.setReplyMarkup(this.replyKeyboardMarkup);
//        return sendMessage;
//    }

    protected void setInfo() {
        this.buttonsList = List.of("Мой профиль", "Поиск аукциона", "Создать аукцион", "FAQ",
                "Вопросы и предложения");
//        replyKeyboardMarkup.setSelective(true);
//        replyKeyboardMarkup.setResizeKeyboard(true);
//        replyKeyboardMarkup.setOneTimeKeyboard(false);
        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRowRow = new KeyboardRow();
        keyboardFirstRowRow.add(new KeyboardButton(buttonsList.get(0)));
        keyboard.add(keyboardFirstRowRow);

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton(buttonsList.get(1)));
        keyboardSecondRow.add(new KeyboardButton(buttonsList.get(2)));

        // Третья строчка клавиатуры
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton(buttonsList.get(3)));
        keyboardThirdRow.add(new KeyboardButton(buttonsList.get(4)));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRowRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);


    }
}
