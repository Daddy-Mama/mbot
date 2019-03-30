package com.example.demo.service;

import com.example.demo.commands.QuestionnareStartMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Артем on 28.03.2019.
 */
@Service
public class QuastionnareService extends BaseService {
    private final CacheService cacheService;

    @Autowired
    public QuastionnareService(CacheService cacheService) {
        allowableCommands.add("Поиск аукциона");
        this.cacheService = cacheService;
        this.SERVICE_ID = 2;
    }

    @Override
    public SendMessage execute(Update update) {
        User user = update.getMessage().getFrom();
        if (null == cacheService.getServiceByUserIdInCache(user.getId())) {
            cacheService.addToCache(this.SERVICE_ID, user.getId());
            return new QuestionnareStartMessage().toSendMessage(update.getMessage().getChatId());
        }
        return null;
    }

    private SendMessage letsStartMessage() {

    }
}
