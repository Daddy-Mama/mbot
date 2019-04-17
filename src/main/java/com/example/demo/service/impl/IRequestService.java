package com.example.demo.service.impl;

import com.example.demo.model.message.MessageTransportDto;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IRequestService {
    MessageTransportDto operatePayment(Update update);
    MessageTransportDto operateCallbackQuery(Update update);
    MessageTransportDto operateMessage(Update update);

}
