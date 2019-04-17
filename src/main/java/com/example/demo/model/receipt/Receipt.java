package com.example.demo.model.receipt;


import com.example.demo.model.dto.Product;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.demo.model.enums.ReservedWordsEnum.*;

public class Receipt {

    private final String title = TITLE_TEXT.getValue();

    private List<Product> orders = new ArrayList<>();

    public Receipt() {
    }

    public void addOrder(Product product) {
        this.orders.add(product);
    }

    public synchronized String toString() {
        return title +
                "\n\n " +
                IntStream.range(1, orders.size())
                        .mapToObj(i -> i + ". " + orders.get(i).toString())
                        .collect(Collectors.joining("\n"))
                + TOTAL_TEXT.getValue()
                + orders.stream().map(x->x.getPrice()).reduce(0,(sum,x)->sum+x)
                + UAH_TEXT.getValue();

    }

    public boolean isEmpty(){
        return this.orders.size()==0;
    }
}
