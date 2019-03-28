package com.example.demo.service;

import com.example.demo.commands.BaseReplyMessage;
import com.example.demo.commands.Commands;
import com.example.demo.interfaces.IBaseService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseService {
    protected List<BaseReplyMessage> allowableCommands = new ArrayList<>();


    public boolean hasCommand(String command) {
        return this.allowableCommands
                .stream()
                .map(x->x.getName())
                .anyMatch(x -> x.toLowerCase().contains(command))
//                .findFirst()
//                .get();
    }

    public SendMessage executeCommand(BaseReplyMessage command){
        // command.execute();
        //return command;
    }
}
