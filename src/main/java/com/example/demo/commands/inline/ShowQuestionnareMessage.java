package com.example.demo.commands.inline;

import com.example.demo.model.Questionnare;
import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ShowQuestionnareMessage extends BaseInlineMessage {
    public ShowQuestionnareMessage() {
        this.text = "Анкета участника";
    }

    @Override
    protected void setInfo() {
        buttonsList.add(new InlineKeyboardButton().setText("Учавствовать").setCallbackData("/participate"));
    }


    @Override
    public MessageTransportDto toMessageTransportDto(Questionnare questionnare) {
        MessageTransportDto messageTransportDto = super.toMessageTransportDto();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setCaption(questionnare.getPreview());
        sendPhoto.setPhoto(questionnare.getPhotoId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(
                "Длительность: " + questionnare.getPeriod() + "\nЦена входа: " + questionnare.getEnterPrice() + "\n");

        messageTransportDto.setEditMessageText(editMessageText);
        messageTransportDto.setSendPhoto(sendPhoto);
        return messageTransportDto;
    }
}
