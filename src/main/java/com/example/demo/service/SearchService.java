package com.example.demo.service;

import com.example.demo.interfaces.ICacheService;
import com.example.demo.interfaces.ISearchService;
import com.example.demo.model.dto.MessageTransportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SearchService extends BaseService implements ISearchService {
    private final ICacheService cacheService;
    @Autowired
    public SearchService(CacheService cacheService){
        this.SERVICE_ID = 3;
        allowableCommands.add("Поиск аукциона");
//        allowableCallbackQueries.add("/start-questionnare");
//        allowableCallbackQueries.add("/save-questionnare");

        this.cacheService = cacheService;
    }
    @Override
    public MessageTransportDto operateMessage(Update update) {
        return null;
    }

    @Override
    public MessageTransportDto operateCallbackQuery(Update update) {
        return null;
    }

    @Override
    public MessageTransportDto operatePhoto(Update update) {
        return null;
    }

}
