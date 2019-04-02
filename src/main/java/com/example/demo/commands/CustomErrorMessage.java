package com.example.demo.commands;

public class CustomErrorMessage extends BaseMessage {
    public CustomErrorMessage(String text) {
        this.text = text;
        addMainMenuButton();
    }

}
