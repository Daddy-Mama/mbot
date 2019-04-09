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
public class QuestionnareDao {

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

    @NotNull
    @Temporal(TemporalType.DATE)
    private LocalDate period;


    public QuestionnareDao() {
    }

    public QuestionnareDao(Questionnare questionnare) {
        this.userName = questionnare.getUser().getUserName();
        this.information = questionnare.getAnswers();
        this.photoId = questionnare.getPhotoId();
        this.enterPrice = questionnare.getEnterPrice();
        this.period = LocalDate.now().plusDays(questionnare.getPeriod());
    }

    public Integer getEnterPrice() {
        return enterPrice;
    }

    public void setEnterPrice(Integer enterPrice) {
        this.enterPrice = enterPrice;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
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
