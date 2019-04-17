package com.example.demo.model.dto;

import com.example.demo.model.enums.ReservedWordsEnum;

public class Product {
    private final String name;
    private final Integer price;

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return this.name +"\n" + price + ReservedWordsEnum.UAH_TEXT;
    }
}
