package com.example.demo.commands.inline;

import com.example.demo.model.QuestionsList;
import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.stream.Collectors;

public class QuestionsMessage extends BaseInlineMessage {
    public QuestionsMessage() {
        this.text = QuestionsList.questions.stream().collect(Collectors.joining("\n"));
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
