package com.example.demo.service.impl;

import com.example.demo.model.dto.CommandsEnum;
import com.example.demo.model.dto.MessageTransportDto;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.model.dto.CommandsEnum.*;

@Service
public class RequestService implements IRequestService {
    private final List<Integer> adminList = Arrays.asList();

    @Override
    public MessageTransportDto operatePayment(Update update) {
        return null;
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        return null;
    }

    @Override
    public MessageTransportDto operateMessage(Update update) {
        if (adminList.contains(update.getMessage().getFrom().getId())) {
            return operateAdminMessage(update);
        } else {
            return operateCustomerMessage(update);


        }
//        return null;
    }

    private MessageTransportDto operateAdminMessage(Update update) {
        return null;
    }

    private MessageTransportDto operateCustomerMessage(Update update) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        String message = update.getMessage().getText();
        if (message.equals(START_TEXT.getValue())) {
            SendMessage sendMessage = new SendMessage();
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
             List<KeyboardRow> rows = new ArrayList<>();
            KeyboardRow firstKeyboarRow = new KeyboardRow();

            firstKeyboarRow.add(new KeyboardButton().setText(PAY_TEXT.getValue()));
            firstKeyboarRow.add(new KeyboardButton().setText(CANCEL_TEXT.getValue()));

            rows.add(firstKeyboarRow);

            replyKeyboardMarkup.setKeyboard(rows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText("Добро пожаловать!");

            messageTransportDto.setSendMessage(sendMessage);
            return messageTransportDto;
        } else {
            return new MessageTransportDto();
        }

    }
}
