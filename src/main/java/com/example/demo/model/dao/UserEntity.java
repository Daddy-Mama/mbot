package com.example.demo.model.dao;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname")
    private String nickname;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "my_questionnare", referencedColumnName = "id")
    private QuestionnareEntity myQuestionnare;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_questionnare",
            joinColumns = {@JoinColumn(name = "questionnare_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private List<QuestionnareEntity> boughtQuestionnaries;

    @Column(name = "pursuit")
    private Integer pursuit;
    @Column(name = "chat_id")
    private Long chatId;

    public UserEntity() {
    }

    public UserEntity(Integer id, String nickname, QuestionnareEntity questionnareEntity, List<QuestionnareEntity> questionnareEntities, Integer pursuit, Long chatId) {
        this.id = Long.valueOf(id);
        this.nickname = nickname;
        this.myQuestionnare = questionnareEntity;
        this.boughtQuestionnaries = questionnareEntities;
        this.pursuit = pursuit;
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Integer getPursuit() {
        return pursuit;
    }

    public void setPursuit(Integer pursuit) {
        this.pursuit = pursuit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public QuestionnareEntity getMyQuestionnare() {
        return myQuestionnare;
    }

    public void setMyQuestionnare(QuestionnareEntity myQuestionnare) {
        this.myQuestionnare = myQuestionnare;
    }

    public List<QuestionnareEntity> getBoughtQuestionnaries() {
        return boughtQuestionnaries;
    }

    public void setBoughtQuestionnaries(List<QuestionnareEntity> boughtQuestionnaries) {
        this.boughtQuestionnaries = boughtQuestionnaries;
    }
}
