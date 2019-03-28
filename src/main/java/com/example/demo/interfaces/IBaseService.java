package com.example.demo.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IBaseService {
//    boolean hasCommand(String command);

    default SendMessage executeCommand(String command) {
        return null;
    }
}
