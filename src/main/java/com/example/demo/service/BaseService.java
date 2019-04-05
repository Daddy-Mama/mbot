package com.example.demo.service;


import com.example.demo.interfaces.IBaseService;
import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseService implements IBaseService {
    protected int SERVICE_ID;
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
                .anyMatch(x -> x.toLowerCase().contains(command));
    }

    @Override
    public boolean hasCallbackQuery(String path) {
        return this.allowableCommands
                .stream()
                .anyMatch(x->x.contains(path));
    }

    public abstract MessageTransportDto operateMessage(Update update);
    public abstract MessageTransportDto operateCallbackQuery(Update update);
}
