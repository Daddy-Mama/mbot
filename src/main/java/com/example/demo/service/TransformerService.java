package com.example.demo.service;

import com.example.demo.commands.SaveQuestionnareMessage;
import com.example.demo.model.dto.MessageTransportDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Service;

@Service
public class TransformerService {

    public String transformToJson(MessageTransportDto messageTransportDto){
        String json = null;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            json = ow.writeValueAsString(messageTransportDto);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
