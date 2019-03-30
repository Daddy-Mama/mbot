package com.example.demo.model;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Артем on 30.03.2019.
 */
public class Questionnare {
    private User user;
    private Integer chatId;
   private List<String> answers = new ArrayList<>();
private boolean isAsked = false;
    public Questionnare(User user) {
        this.user = user;
    }

    public Questionnare(User user, int chatId, List<String> answers) {
        this.user = user;
        this.chatId = chatId;
        this.answers = answers;
    }

    public Questionnare() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void addAnswer(String answer) {
        this.answers.add(answer);
    }
    public boolean isAsked(){
        return isAsked;
    }

    public void setAsked(boolean asked) {
        isAsked = asked;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}