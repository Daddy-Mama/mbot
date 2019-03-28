package com.example.demo.service;

import com.example.demo.commands.Command;
import com.example.demo.commands.Commands;
import com.example.demo.interfaces.IMainMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.commands.Commands.START;

@Service
public class MainMenuService {

//    protected List<Command> allowableCommands = new ArrayList<>();
protected List<String> allowableCommands = new ArrayList<>();
    private List<Integer> chatIdInServiceList = new ArrayList<>();

    protected ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();


    @Autowired
    public MainMenuService() {
       setReplyKeyboardMarkup();
       allowableCommands.add("Command");
    }

    private void setReplyKeyboardMarkup(){
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Мой профиль"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Поиск аукциона"));
        keyboardSecondRow.add(new KeyboardButton("Создать аукцион"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardThirdRow.add(new KeyboardButton("FAQ"));
        keyboardThirdRow.add(new KeyboardButton("Support"));


        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    public boolean hasUserId(int id) {
        return chatIdInServiceList.contains(id);
    }

    public boolean hasCommand(String command) {
        return this.allowableCommands
                .stream()
//                .map(x -> x.getName())
                .anyMatch(x -> x.toLowerCase().contains(command));
//                .findFirst()
//                .get();
    }

    public SendMessage execute(Update update) {
        return showMainMenu(update);
    }

    public SendMessage showMainMenu(Update update) {
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId()," Privet, Che");
        sendMessage.setReplyMarkup(this.replyKeyboardMarkup);
        return sendMessage;
    }


}
