package com.example.demo.interfaces.repositories;

import com.example.demo.model.dao.QuestionnareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnareDaoRepository extends JpaRepository<QuestionnareEntity,Long> {
    QuestionnareEntity findByUserName(String name);
}
