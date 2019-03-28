package com.example.demo.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ITranslatorService {
    public SendMessage executeCommand(Update update);
}
