package com.example.demo.service;

import com.example.demo.commands.inline.CustomErrorMessage;
import com.example.demo.interfaces.IDatabaseService;
import com.example.demo.interfaces.IPaymentService;
import com.example.demo.model.dao.QuestionnareEntity;
import com.example.demo.model.dto.MessageTransportDto;
import com.example.demo.model.dto.NotificationMessage;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PaymentService implements IPaymentService {
    @Autowired
    private IDatabaseService databaseService;

    @Override
    public MessageTransportDto buyDate(String username) {
        QuestionnareEntity questionnareEntity = databaseService.findQuestionnareByUserName(username);
        if (questionnareEntity == null) {
            return new CustomErrorMessage("Информация об анкете не найдена").toMessageTransportDto();
        }
        SendInvoice sendInvoice = new SendInvoice();
        sendInvoice.setTitle("Оплата встречи");
        sendInvoice.setDescription("Вы оплачиваете встречу с @" + username);
        sendInvoice.setPayload("@" + username);
        sendInvoice.setStartParameter("/buy-date/" + username);
        sendInvoice.setProviderToken("632593626:TEST:i85538881542");
        sendInvoice.setCurrency("UAH");
        List<LabeledPrice> labeledPrices = new ArrayList<>();
        labeledPrices.add(new LabeledPrice("Super price label", questionnareEntity.getEnterPrice() * 100));
        sendInvoice.setPrices(labeledPrices);
        sendInvoice.setPhotoUrl("AgADAgADnqoxG_q_aUloLh3Aq091eSRFXw8ABGOtqfvVe9KM940DAAEC");
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        messageTransportDto.setSendInvoice(sendInvoice);
        return messageTransportDto;
    }

    @Override
    public MessageTransportDto onSuccessPayment(Update update) {
        String name = update.getMessage().getSuccessfulPayment().getInvoicePayload();
        User user = update.getMessage().getFrom();
        databaseService.addMeeting(user, name, update.getMessage().getChatId());
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        messageTransportDto.setNotificationMessage(new NotificationMessage(user.getUserName(), update.getMessage().getChatId()));
        messageTransportDto.setSendMessage(new SendMessage().setText("Встреча успешно оформлена!Я попрошу " + name + " связаться с тобой!\n Но ты всегда можешь написать самостоятельно) \n\n Если встреча не состоится по каким-либо причинам напиши в поддержку и тебе вернут деньги! :)"));
        return messageTransportDto;
    }
}
