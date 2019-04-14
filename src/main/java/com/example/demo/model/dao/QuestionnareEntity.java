package com.example.demo.model.dao;

import com.example.demo.model.Questionnare;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Entity
@Table(name = "questionnare")
public class QuestionnareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_name")
    private String userName;

    @NotNull
    @Column(name = "information")
    private String information;

    @NotNull
    @Column(name = "photo_id")
    private String photoId;

    @NotNull
    @Column(name = "enter_price")
    private Integer enterPrice;


    public QuestionnareEntity() {
    }

    public QuestionnareEntity(Questionnare questionnare) {
        this.userName = questionnare.getUser().getUserName();
        this.information = questionnare.getAnswers();
        this.photoId = questionnare.getPhotoId();
        this.enterPrice = questionnare.getEnterPrice();
     }




    public Integer getEnterPrice() {
        return enterPrice;
    }

    public void setEnterPrice(Integer enterPrice) {
        this.enterPrice = enterPrice;
    }


    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Long getId() {
        return id;
    }
}
