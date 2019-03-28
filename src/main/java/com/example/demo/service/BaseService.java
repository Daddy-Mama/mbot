package com.example.demo.service;


import com.example.demo.commands.Command;
import com.example.demo.commands.Commands;
import com.example.demo.interfaces.IBaseService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseService {

    protected List<Command> allowableCommands = new ArrayList<>();
    private List<Integer> chatIdInServiceList = new ArrayList<>();



    public boolean hasUserId(int id){
        return chatIdInServiceList.contains(id);
    }
    public boolean hasCommand(String command) {
        return this.allowableCommands
                .stream()
                .map(x -> x.getName())
                .anyMatch(x -> x.toLowerCase().contains(command));
//                .findFirst()
//                .get();
    }

//    public String parseMessage(String message) {
//        return message.split(" ")[0];
//
//    }

//    public SendMessage executeCommand(Update update) {
//
//        String command = parseMessage(update.getMessage().getText());
//
//        assert (hasCommand(command));
//        //create command from string info and execute this
//        SendMessage sendMessage = command.execute();
//        return sendMessage;
//    }
}
