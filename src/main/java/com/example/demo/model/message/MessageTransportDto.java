package com.example.demo.model.message;

import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class MessageTransportDto {
    private SendMessage sendMessage;
    private SendPhoto sendPhoto;
    private EditMessageText editMessageText;
    private DeleteMessage deleteMessage;
    private SendInvoice sendInvoice;
    private AnswerPreCheckoutQuery answerPreCheckoutQuery;
    private  NotificationMessage notificationMessage;
    public MessageTransportDto() {
    }

    public AnswerPreCheckoutQuery getAnswerPreCheckoutQuery() {
        return answerPreCheckoutQuery;
    }

    public void setAnswerPreCheckoutQuery(AnswerPreCheckoutQuery answerPreCheckoutQuery) {
        this.answerPreCheckoutQuery = answerPreCheckoutQuery;
    }

    public NotificationMessage getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(NotificationMessage notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public DeleteMessage getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(DeleteMessage deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public SendInvoice getSendInvoice() {
        return sendInvoice;
    }

    public void setSendInvoice(SendInvoice sendInvoice) {
        this.sendInvoice = sendInvoice;
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    public void setSendPhoto(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    public EditMessageText getEditMessageText() {
        return editMessageText;
    }

    public void setEditMessageText(EditMessageText editMessageText) {
        this.editMessageText = editMessageText;
    }
}
