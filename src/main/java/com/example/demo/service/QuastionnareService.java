package com.example.demo.service;

import com.example.demo.commands.MainMenuMessage;
import com.example.demo.commands.QuestionnareStartMessage;
import com.example.demo.commands.QuestionsMessage;
import com.example.demo.model.Questionnare;
import com.example.demo.model.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by Артем on 28.03.2019.
 */
@Service
public class QuastionnareService extends BaseService {
    private final CacheService cacheService;
    private ConcurrentHashMap<Integer, Questionnare> userOnStage = new ConcurrentHashMap<>();

    @Autowired
    public QuastionnareService(CacheService cacheService) {
        allowableCommands.add("Создать аукцион");
        this.cacheService = cacheService;
        this.SERVICE_ID = 2;
    }

    @Override
    public SendMessage execute(Update update, boolean isCommand) {
        User user = update.getMessage().getFrom();
        if (isCommand) {
            cacheService.addToCache(this.SERVICE_ID, user.getId());
            addUserToStageCache(user);
            return letsStartMessage(update);
        } else {
            return askQuestion(update);
        }
//        if (null == cacheService.getServiceByUserIdInCache(user.getId())) {
//            cacheService.addToCache(this.SERVICE_ID, user.getId());
//            return new QuestionnareStartMessage().toSendMessage(update.getMessage().getChatId());
//        }

    }

    //add user to stageCache, if he is in cache delete and add again(clear history)
    private void addUserToStageCache(User user) {
        if (userOnStage.containsKey(user.getId())) {
            userOnStage.remove(user.getId());
        }
        userOnStage.put(user.getId(), new Questionnare(user));
    }

    private SendMessage letsStartMessage(Update update) {
        return new QuestionnareStartMessage().toSendMessage(update.getMessage().getChatId());
    }

    private SendMessage askQuestion(Update update) {
        User user = update.getMessage().getFrom();
        Questionnare questionnare = userOnStage.get(user.getId());
        if (!questionnare.isAsked()) {
            questionnare.setAsked(true);
            return new QuestionsMessage().toSendMessage(update.getMessage().getChatId());
        } else {
            //ask him for photo
            List<String> answers = Arrays.asList(update.getMessage().getText().split("\n"));
            //check if answers are in correct form
            SendMessage sendMessage = validateAnswers(answers,update);
            if (sendMessage!=null){
                return sendMessage;
            }
            questionnare.setAnswers(answers);
            //save anketa to DB


            return new MainMenuMessage("Теперь ты участник").toSendMessage(update.getMessage().getChatId());
        }
    }

    private SendMessage validateAnswers(List<String> answers,Update update) {
//        Pattern p = Pattern.compile("^\\d{1,2}[.] [a-zA-Z]+");
//        Optional<String> badLine = answers.stream().filter(x -> !p.matcher(x.trim()).find()).findAny();
//        if (badLine.isPresent()) {
//            SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), "Неправильная строка: " + badLine.get());
//            return sendMessage;
//        }
        if (answers.size() != Questions.questions.size()){
            SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), "Ответь на все вопросы одной строкой!");
            return sendMessage;
        }
        return null;
    }
}
