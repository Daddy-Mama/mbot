package com.example.demo.commands.inline;

import com.example.demo.model.Questionnare;
import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ShowQuestionnareMessage extends BaseInlineMessage {
    public ShowQuestionnareMessage(String username) {
        buttonsList.add(new InlineKeyboardButton().setText("Купить свидание").setCallbackData("/buy-date/"+username));
    }




    @Override
    public MessageTransportDto toMessageTransportDto(Questionnare questionnare) {
        MessageTransportDto messageTransportDto = super.toMessageTransportDto();

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setCaption(questionnare.getPreview());
        sendPhoto.setPhoto(questionnare.getPhotoId());

        messageTransportDto.setSendPhoto(sendPhoto);
        return messageTransportDto;
    }
}
