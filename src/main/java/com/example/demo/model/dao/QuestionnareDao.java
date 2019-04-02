package com.example.demo.model.dao;

import com.example.demo.model.Questionnare;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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

    public QuestionnareDao() {
    }

    public QuestionnareDao(Questionnare questionnare){
        this.userName = questionnare.getUser().getUserName();
        this.information = questionnare.getAnswers().stream().collect(Collectors.joining("\n"));
    }

    public String getUserId() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userName = userId;
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
