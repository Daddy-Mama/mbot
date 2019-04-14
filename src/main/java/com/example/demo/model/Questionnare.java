package com.example.demo.model;

import com.example.demo.model.dao.QuestionnareEntity;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Артем on 30.03.2019.
 */
public class Questionnare {
    private User user;
    private String answers;
    private String photoId;
     private Integer enterPrice;

    public Questionnare(User user) {
        this.user = user;
    }


    public Questionnare(QuestionnareEntity questionnareEntity) {
        this.answers = questionnareEntity.getInformation();
        this.photoId = questionnareEntity.getPhotoId();
         this.enterPrice = questionnareEntity.getEnterPrice();
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



    public Integer getEnterPrice() {
        return enterPrice;
    }

    public void setEnterPrice(Integer enterPrice) {
        this.enterPrice = enterPrice;
    }


    public void setAnswers(List<String> answers) {
        this.answers = answers.stream().collect(Collectors.joining("\n"));
    }

    public boolean isFull() {
        return answers != null && photoId != null  && enterPrice != null;
    }

    public String getPreview() {
        String preview = "Об участнике:\n" + this.answers ;
        if (this.enterPrice > 0) {
            preview = preview + "\nЦена участия: " + enterPrice;
        }
        return preview;
    }


    @Override
    public String toString() {
        return "Questionnare{" +
                "user=" + user +
                ", answers='" + answers + '\'' +
                ", photoId='" + photoId + '\'' +
                 ", enterPrice=" + enterPrice;
    }
}
