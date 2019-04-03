package com.example.demo.bot;


import com.example.demo.interfaces.ITranslatorService;
import com.example.demo.interfaces.IMainMenuService;
import com.example.demo.model.dto.MessageTransportDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

@Component
@PropertySource("classpath:application.properties")
public class Bot extends TelegramLongPollingBot {
    private final static Logger logger = LogManager.getLogger();

    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.username}")
    private String name;


    @Deprecated
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();


    private final ITranslatorService translatorService;

    @Autowired
    public Bot(ITranslatorService translatorService) {
//        this.IMainMenuService = IMainMenuService;
        this.translatorService = translatorService;
    }


    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */


    @Override
    public void onUpdateReceived(Update update) {
        logger.info("===============================================================================================");
        SendMessage sendMessage = translatorService.executeCommand(update);

        buildAnswer(sendMessage, update);

    }

    public synchronized void sendMsg(SendMessage sendMessage) {
        try {
            hideKeyboard(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void buildAnswer(SendMessage sendMessage, Update update) {
        try {
            Gson gson = new Gson();
            MessageTransportDto messageTransportDto = gson.fromJson(sendMessage.getText(),
                                                                    MessageTransportDto.class);
            if (messageTransportDto.getPhotoId() != null) {
                String f_id = messageTransportDto.getPhotoId();
                SendPhoto photo = new SendPhoto()
                        .setChatId(update.getMessage().getChatId())
                        .setPhoto(f_id)
                        .setCaption(messageTransportDto.getText());
                hideKeyboard(photo);
                execute(photo);
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            sendMsg(sendMessage);
        }
    }


    private void hideKeyboard(SendMessage sendMessage){
        if (sendMessage.getReplyMarkup()==null){
            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
            sendMessage.setReplyMarkup(replyKeyboardRemove);
        }
    }
    private void hideKeyboard(SendPhoto sendPhoto){
        if (sendPhoto.getReplyMarkup()==null){
            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
            sendPhoto.setReplyMarkup(replyKeyboardRemove);
        }
    }

    @Deprecated
    public void doSmth() {
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboard = new ArrayList<>(3);

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("1");

        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("2");
        keyboardSecondRow.add("3");
        keyboard.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("4");
        keyboardThirdRow.add("5");
        keyboard.add(keyboardThirdRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
//        return keyboard;

    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return name;
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    @Override
    public String getBotToken() {
        return token;
    }
}
