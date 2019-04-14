package com.example.demo.service;


import com.example.demo.interfaces.IBaseService;
import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseService implements IBaseService {
    protected Integer SERVICE_ID;
    protected List<String> allowableCommands = new ArrayList<>();
    protected List<String> allowableCallbackQueries = new ArrayList<>();

    public int getSERVICE_ID() {
        return SERVICE_ID;
    }

    public BaseService() {
    }


    public boolean hasCommand(String command) {
        return this.allowableCommands
                .stream()
                .anyMatch(x -> command.toLowerCase().contains(x.toLowerCase()));
    }

    @Override
    public boolean hasCallbackQuery(String path) {
        return this.allowableCallbackQueries
                .stream()
                .anyMatch(x -> path.contains(x));
    }

    public abstract MessageTransportDto operateMessage(Update update);

    public MessageTransportDto operateCallbackQuery(Update update) {
        MessageTransportDto messageTransportDto;


        switch (update.getCallbackQuery().getData()) {
            case "/back/menu": {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                messageTransportDto = new MessageTransportDto();
                messageTransportDto.setDeleteMessage(deleteMessage);
                return messageTransportDto;
            }

        }
        return null;
    }

    public abstract MessageTransportDto operatePhoto(Update update);
}
