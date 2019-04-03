package com.example.demo.model.dto;

public class MessageTransportDto {
    private String text;
    private String photoId;

    public MessageTransportDto() {
    }

    public MessageTransportDto(String text, String photoId) {
        this.text = text;
        this.photoId = photoId;
    }

    public MessageTransportDto(String text) {
        this.text = text;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
