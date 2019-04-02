package com.example.demo.commands;

public class UploadPhotoRequestMessage extends BaseMessage {
    public UploadPhotoRequestMessage() {
        setInfo();
       addMainMenuButton();
    }

    @Override
    protected void setInfo() {
        this.text = "Теперь загрузи фото, которое увидят остальные=)";
    }
}
