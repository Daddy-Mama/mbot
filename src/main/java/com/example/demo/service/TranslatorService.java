package com.example.demo.service;


import com.example.demo.commands.MainMenuMessage;
import com.example.demo.commands.inline.CustomErrorMessage;
import com.example.demo.interfaces.*;
import com.example.demo.model.dto.MessageTransportDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class TranslatorService implements ITranslatorService {
    private final static Logger logger = LogManager.getLogger();

    private final ICacheService cacheService;

    private final Map<Integer, IBaseService> baseServiceMap;
    @Autowired
    private   IPaymentService paymentService;

    @Autowired
    public TranslatorService(CacheService cacheService,
                             MainMenuService mainMenuService,
                             QuastionnareService quastionnareService,
                             SearchService searchService) {

        this.cacheService = cacheService;
        baseServiceMap = new HashMap<>();
        baseServiceMap.put(mainMenuService.getSERVICE_ID(), mainMenuService);
        baseServiceMap.put(quastionnareService.getSERVICE_ID(), quastionnareService);
        baseServiceMap.put(searchService.getSERVICE_ID(),searchService);
    }


    public MessageTransportDto operateMessage(Update update) {
        User user = update.getMessage().getFrom();
        String command = parseMessage(update.getMessage().getText());
        logger.info("New update received: " + user.getId() + " " + user.getUserName());

        IBaseService service = getServiceForUser(user.getId());

        //user is in cache
        //it means that message is received  but NOT COMMAND
        if (service != null) {
            return registeredUserMessage(service, update);
        }
        //check if this IS COMMAND
        if (command != null) {
            for (IBaseService baseService : baseServiceMap.values()) {
                if (baseService.hasCommand(command)) {
                    return baseService.operateMessage(update);

                }
            }
        }
        return errorMessage(update);
    }


    public MessageTransportDto operateCallbackQuery(Update update) {
        String call_data = update.getCallbackQuery().getData();
//        long message_id = update.getCallbackQuery().getMessage().getMessageId();
//        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        MessageTransportDto messageTransportDto = null;
        IBaseService service;

        if (call_data != null) {
            for (IBaseService baseService : baseServiceMap.values()) {
                if (baseService.hasCallbackQuery(call_data)) {
                    service = baseService;
                    messageTransportDto = service.operateCallbackQuery(update);
                    break;
                }
            }
        }
        return messageTransportDto;
    }

    @Override
    public MessageTransportDto operatePhoto(Update update) {
        User user = update.getMessage().getFrom();
        IBaseService service = getServiceForUser(user.getId());
        if (service!=null){
            return service.operatePhoto(update);
        }
        else {
            return new CustomErrorMessage("Ошибка обработки фотографии").toMessageTransportDto();
        }
    }

    public MessageTransportDto operatePayment(Update update){
       return paymentService.onSuccessPayment(update);
    }

    private MessageTransportDto registeredUserMessage(IBaseService service, Update update) {
        MessageTransportDto answer = service.operateMessage(update);
        return answer;
    }

    private MessageTransportDto errorMessage(Update update) {
        return new MainMenuMessage(
                "Что-то странное.. Может ты предыдущую сессию не закончил:( Верну тебя в меню").toMessageTransportDto(
                update.getMessage().getChatId());
    }

    private IBaseService getServiceForUser(int userId) {
        return baseServiceMap.get(cacheService.getServiceByUserIdInCache(userId));
    }

    private final String parseMessage(String parsingMessage) {
        if (parsingMessage != null) {
            return parsingMessage.toLowerCase();
        } else {
            return null;
        }

    }
}
