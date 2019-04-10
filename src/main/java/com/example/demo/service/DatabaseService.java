package com.example.demo.service;

import com.example.demo.commands.inline.CustomErrorMessage;
import com.example.demo.commands.inline.QuestionnareSavedMessage;
import com.example.demo.interfaces.IDatabaseService;
import com.example.demo.interfaces.repositories.QuestionnareDaoRepository;
import com.example.demo.model.Questionnare;
import com.example.demo.model.dao.QuestionnareDao;
import com.example.demo.model.dto.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DatabaseService implements IDatabaseService {
    private final QuestionnareDaoRepository repository;

    @Autowired
    public DatabaseService(QuestionnareDaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MessageTransportDto saveQuestionnare(Questionnare questionnare) {
        if (repository.findByUserName(questionnare.getUser().getUserName()) == null) {

            QuestionnareDao questionnareDao = new QuestionnareDao(questionnare);
            repository.saveAndFlush(questionnareDao);
            return new QuestionnareSavedMessage().toMessageTransportDto();
        }
        else return new CustomErrorMessage("У тебя уже есть активные анкеты").toMessageTransportDto();

    }
}
