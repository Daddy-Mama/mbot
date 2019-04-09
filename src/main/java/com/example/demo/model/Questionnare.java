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
    private Integer period;
    private Integer enterPrice;
    private Integer status;

    public Questionnare(User user) {
        this.user = user;
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

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getEnterPrice() {
        return enterPrice;
    }

    public void setEnterPrice(Integer enterPrice) {
        this.enterPrice = enterPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers.stream().collect(Collectors.joining("\n"));
    }

    public boolean isFull() {
        return answers != null && photoId != null && period !=null && enterPrice !=null && status!=null;
    }
}
