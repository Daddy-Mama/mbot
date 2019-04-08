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
    private String answers;
    private String photoId;
    private int period;
    private int enterPrice;
    private int status;

    public Questionnare(User user) {
        this.user = user;
    }


    public Questionnare() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getEnterPrice() {
        return enterPrice;
    }

    public void setEnterPrice(int enterPrice) {
        this.enterPrice = enterPrice;
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


    public String getAnswers() {
        return answers;
    }


    public void setAnswers(List<String> answers) {

        this.answers = answers.stream().collect(Collectors.joining("\n"));
    }

    public boolean isFull() {
        return answers != null && photoId != null;
    }
}
