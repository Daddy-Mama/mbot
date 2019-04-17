package com.example.demo.model.enums;

public enum  CommandsEnum {
    START_TEXT("/start"),
    MAKE_ORDER_TEXT("Сделать заказ"),
    PAY_TEXT("Оплатить"),
    CANCEL_TEXT("Отмена");


    private String value;

    private CommandsEnum(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }
}
