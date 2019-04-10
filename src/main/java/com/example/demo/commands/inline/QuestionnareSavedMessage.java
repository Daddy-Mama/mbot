package com.example.demo.commands.inline;

import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class QuestionnareSavedMessage extends BaseInlineMessage {
    public QuestionnareSavedMessage() {
        this.text ="Отлично! Теперь ты - участник!)";
    }
    @Override
    public MessageTransportDto toMessageTransportDto() {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(this.text);
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        messageTransportDto.setEditMessageText(editMessageText);
        return messageTransportDto;
    }
}
