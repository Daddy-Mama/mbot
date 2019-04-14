package com.example.demo.commands.inline;

import com.example.demo.model.Questionnare;
import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ApproveQuestionnareMessage extends BaseInlineMessage {

    public ApproveQuestionnareMessage() {
        this.text = "Вот твоя анкета. Как тебе?";
        setInfo();
    }

    @Override
    protected void setInfo() {
        buttonsList.add(new InlineKeyboardButton().setText("Анкета норм. Save").setCallbackData("/save-questionnare"));
    }

    @Override
    public MessageTransportDto toMessageTransportDto(Questionnare questionnare) {
        MessageTransportDto messageTransportDto = super.toMessageTransportDto();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setCaption(questionnare.getPreview());
        sendPhoto.setPhoto(questionnare.getPhotoId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(
                  "\nЦена: " + questionnare.getEnterPrice());

        messageTransportDto.setEditMessageText(editMessageText);
        messageTransportDto.setSendPhoto(sendPhoto);
        return messageTransportDto;
    }
}
