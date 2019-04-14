package com.example.demo.interfaces;

import com.example.demo.model.Questionnare;
import com.example.demo.model.dao.QuestionnareEntity;
import com.example.demo.model.dto.MessageTransportDto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public interface IDatabaseService {
     MessageTransportDto saveQuestionnare(Questionnare questionnare, Long chatId);
     MessageTransportDto findByUserName(String username);
     QuestionnareEntity findQuestionnareByUserName(String username);
     void addMeeting(User user, String ownerName, Long chatId);
}
