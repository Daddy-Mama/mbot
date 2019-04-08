package com.example.demo.service;

import com.example.demo.interfaces.IDatabaseService;
import com.example.demo.interfaces.repositories.QuestionnareDaoRepository;
import com.example.demo.model.Questionnare;
import com.example.demo.model.dao.QuestionnareDao;
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
    public void saveQuestionnare(Questionnare questionnare) {
        QuestionnareDao questionnareDao = new QuestionnareDao(questionnare);
        repository.saveAndFlush(questionnareDao);
    }
}
