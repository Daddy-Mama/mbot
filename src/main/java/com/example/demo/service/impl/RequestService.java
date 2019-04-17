package com.example.demo.service.impl;

import com.example.demo.model.dto.Menu;
import com.example.demo.model.message.MessageTransportDto;
import com.example.demo.model.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.demo.model.enums.CommandsEnum.*;
import static com.example.demo.model.enums.ReservedWordsEnum.HELLO_TEXT;

@Service
public class RequestService implements IRequestService {
    private final List<Integer> adminChatList = Arrays.asList();
    private Map<Receipt, User> receipts = new ConcurrentHashMap<>();

    @Autowired
    private Menu menu;

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
        if (adminChatList.contains(update.getMessage().getChatId())) {
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
            sendMessage.setText(HELLO_TEXT.getValue());

            messageTransportDto.setSendMessage(sendMessage);
            return messageTransportDto;
        } else {
            return new MessageTransportDto();
        }
    }

    private MessageTransportDto buildMenu(Receipt receipt){
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        if (receipt==null){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(menu.getHeader());
            sendMessage.setReplyMarkup(menu.getInlineKeyboardMarkup());

            messageTransportDto.setSendMessage(sendMessage);
            return messageTransportDto;
        }
    }
}
