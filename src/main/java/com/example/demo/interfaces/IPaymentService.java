package com.example.demo.interfaces;

import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IPaymentService {
    MessageTransportDto buyDate(String username);
    MessageTransportDto onSuccessPayment(Update update);
}
