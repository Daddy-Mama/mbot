package com.example.demo.commands;

import com.example.demo.model.QuestionsList;

import java.util.stream.Collectors;

/**
 * Created by Артем on 30.03.2019.
 */
public class QuestionsMessage extends BaseMessage {
    public QuestionsMessage() {
        setInfo();
        addMainMenuButton();
    }

    @Override
    protected void setInfo() {
        this.text = QuestionsList.questions.stream().collect(Collectors.joining("\n"));

    }
}
