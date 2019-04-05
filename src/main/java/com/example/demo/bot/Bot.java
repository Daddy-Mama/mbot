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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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

        MessageTransportDto messageTransportDto = new MessageTransportDto();
        if (update.hasCallbackQuery()) {
            messageTransportDto = operateCallbackQuery(update);
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            messageTransportDto = operateMessage(update);
        }


        try {
            buildAnswer(messageTransportDto, update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private MessageTransportDto operateCallbackQuery(Update update) {
        return translatorService.operateCallbackQuery(update);
    }


    private MessageTransportDto operateMessage(Update update) {
        return translatorService.operateMessage(update);
    }


    public synchronized void buildAnswer(MessageTransportDto messageTransportDto, Update update)
            throws TelegramApiException {

        if (messageTransportDto.getEditMessageText() != null) {
            EditMessageText editMessageText = messageTransportDto.getEditMessageText();
            editMessageText.setChatId(update.getMessage().getChatId());

            int message_id = 0;
            long chat_id = 0;
            if (update.hasCallbackQuery()) {
                message_id = update.getCallbackQuery().getMessage().getMessageId();
                chat_id = update.getCallbackQuery().getMessage().getChatId();

            } else {
                message_id = update.getMessage().getMessageId();
                chat_id = update.getMessage().getChatId();
            }

            editMessageText.setChatId(chat_id);
            editMessageText.setMessageId(message_id);
            execute(editMessageText);
        }

        if (messageTransportDto.getSendMessage() != null) {

            SendMessage sendMessage = messageTransportDto.getSendMessage();
            sendMessage.setChatId(update.getMessage().getChatId());

            execute(sendMessage);
        }

        if (messageTransportDto.getSendPhoto() != null) {

            SendPhoto sendPhoto = messageTransportDto.getSendPhoto();
            sendPhoto.setChatId(update.getMessage().getChatId());

            execute(sendPhoto);
        }
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
