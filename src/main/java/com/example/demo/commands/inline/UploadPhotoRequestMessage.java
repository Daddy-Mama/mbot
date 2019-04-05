package com.example.demo.commands.inline;

import com.example.demo.commands.inline.BaseInlineMessage;

public class UploadPhotoRequestMessage extends BaseInlineMessage {
    public UploadPhotoRequestMessage() {
        setInfo();
    }

    @Override
    protected void setInfo() {
        this.text = "Теперь загрузи фото, которое увидят остальные=)";
    }
}
