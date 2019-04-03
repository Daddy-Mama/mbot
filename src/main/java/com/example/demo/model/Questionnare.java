package com.example.demo.model;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Артем on 30.03.2019.
 */
public class Questionnare {
    private User user;
    private Integer chatId;
    private  String answers;
    private boolean inProgress = false;
    private String photoId;

    public Questionnare(User user) {
        this.user = user;
    }

    public Questionnare(User user, int chatId, String answers) {
        this.user = user;
        this.chatId = chatId;
        this.answers = answers;
    }

    public Questionnare() {
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
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

    public String getAnswers() {
        return answers;
    }


    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public void setAnswers(List<String> answers) {

        this.answers = answers.stream().collect(Collectors.joining("\n"));
    }
    public boolean isFull(){
        return answers!=null && photoId!=null && inProgress;
    }
}
