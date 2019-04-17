package com.example.demo.model.dto;

import com.example.demo.model.enums.ReservedWordsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.model.enums.ReservedWordsEnum.MENU_ADDITIONAL_INFO_TEXT;
import static com.example.demo.model.enums.ReservedWordsEnum.MENU_TITLE_TEXT;

@Component
public class Menu {
//    private final SendMessage sendMessage;
    private final List<Product> products = new ArrayList<>();
    private final InlineKeyboardMarkup inlineKeyboardMarkup;
    private final String header;

    @Autowired
    public Menu() {
        for (int i = 1; i < 5; i++) {
            this.products.add(new Product("product_" + i, i));
        }
//        this.sendMessage = new SendMessage();
//build text part
        this.header = MENU_TITLE_TEXT.getValue() + "\n\n" + MENU_ADDITIONAL_INFO_TEXT.getValue();
//        sendMessage.setText(text);
//build inline keyboard
        List<List<InlineKeyboardButton>> buttons =
                products.stream()
                        .map(x -> new InlineKeyboardButton(x.getName()))
                        .map(x -> {
                            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
                            keyboardButtonsRow.add(x);
                            return keyboardButtonsRow;
                        })
                        .collect(Collectors.toList());

        this.inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);

//        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        return inlineKeyboardMarkup;
    }

    public String getHeader() {
        return header;
    }
}
