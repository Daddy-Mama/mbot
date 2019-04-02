package com.example.demo.interfaces.repositories;

import com.example.demo.model.dao.QuestionnareDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnareDaoRepository extends JpaRepository<QuestionnareDao,Long> {
}
