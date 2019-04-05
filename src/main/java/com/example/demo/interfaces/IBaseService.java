package com.example.demo.interfaces;

import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IBaseService {

    boolean hasCommand(String command);
    boolean hasCallbackQuery(String path);
    MessageTransportDto operateMessage(Update update );
    MessageTransportDto operateCallbackQuery(Update update);
    int getSERVICE_ID();


}
