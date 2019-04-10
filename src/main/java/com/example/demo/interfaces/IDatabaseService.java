package com.example.demo.interfaces;

import com.example.demo.model.Questionnare;
import com.example.demo.model.dto.MessageTransportDto;

public interface IDatabaseService {
     MessageTransportDto saveQuestionnare(Questionnare questionnare);
}
