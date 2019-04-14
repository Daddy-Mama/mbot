package com.example.demo.service;

import com.example.demo.commands.inline.CustomErrorMessage;
import com.example.demo.commands.inline.QuestionnareSavedMessage;
import com.example.demo.commands.inline.ShowQuestionnareMessage;
import com.example.demo.interfaces.IDatabaseService;
import com.example.demo.interfaces.repositories.QuestionnareDaoRepository;
import com.example.demo.interfaces.repositories.UserRepository;
import com.example.demo.model.Questionnare;
import com.example.demo.model.dao.QuestionnareEntity;
import com.example.demo.model.dao.UserEntity;
import com.example.demo.model.dto.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.transaction.Transactional;

@Service
public class DatabaseService implements IDatabaseService {
    @Autowired
    private QuestionnareDaoRepository questionnareDaoRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public MessageTransportDto saveQuestionnare(Questionnare questionnare, Long chatId) {
        User user = questionnare.getUser();
        UserEntity userEntity = getUser(user, chatId);
        if (userEntity.getMyQuestionnare() == null) {
            QuestionnareEntity questionnareEntity = new QuestionnareEntity(questionnare);
//            questionnareDaoRepository.saveAndFlush(questionnareEntity);
            userEntity.setMyQuestionnare(questionnareEntity);
            userRepository.saveAndFlush(userEntity);
            return new QuestionnareSavedMessage().toMessageTransportDto();
        } else {
            return new CustomErrorMessage("У тебя уже есть активные анкеты").toMessageTransportDto();
        }

    }

    @Transactional
    public UserEntity getUser(User user, Long chatId) {
        if (!userRepository.existsById(Long.valueOf(user.getId()))) {
            UserEntity userEntity = new UserEntity(user.getId(), user.getUserName(), null, null, 0, chatId);
            return userRepository.saveAndFlush(userEntity);
        } else {
            return userRepository.findById(Long.valueOf(user.getId())).get();
        }

    }

    @Transactional
    public MessageTransportDto findByUserName(String username) {
        if (questionnareDaoRepository.findByUserName(username) == null) {
            QuestionnareEntity questionnareEntity = questionnareDaoRepository.findByUserName(username);
            Questionnare questionnare = new Questionnare(questionnareEntity);
            return new ShowQuestionnareMessage(username).toMessageTransportDto(questionnare);
        } else return new CustomErrorMessage("Анкета такого пользователя не найдена").toMessageTransportDto();
    }

    @Transactional
    public QuestionnareEntity findQuestionnareByUserName(String username){
        return questionnareDaoRepository.findByUserName(username);
    }

    @Transactional
    public void addMeeting(User user, String ownerName, Long chatId) {
        UserEntity userEntity = getUser(user, chatId);
        QuestionnareEntity questionnareEntity = questionnareDaoRepository.findByUserName(ownerName);
        userEntity.getBoughtQuestionnaries().add(questionnareEntity);
        userRepository.save(userEntity);
    }
}
