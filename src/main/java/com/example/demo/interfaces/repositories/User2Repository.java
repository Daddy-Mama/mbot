package com.example.demo.interfaces.repositories;

import com.example.demo.model.dao.User2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User2Repository extends JpaRepository<User2Entity,Long> {
}
