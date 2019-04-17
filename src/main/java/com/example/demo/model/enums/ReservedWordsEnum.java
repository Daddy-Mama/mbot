package com.example.demo.model.enums;

public enum  ReservedWordsEnum {
    TITLE_TEXT("Ваш заказ: "),
    TOTAL_TEXT("Всего к оплате: "),
    UAH_TEXT("грн. "),
    HELLO_TEXT("Добро пожаловать!"),

    MENU_TITLE_TEXT("Menu title"),
    MENU_ADDITIONAL_INFO_TEXT("Menu additional info");
    private String value;

    private ReservedWordsEnum(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }
}
