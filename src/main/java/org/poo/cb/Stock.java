package org.poo.cb;

import java.util.ArrayList;

public class Stock {
    private String stockName;
    private Integer amount;

    public Stock(String stockName, Integer amount) {
        this.stockName = stockName;
        this.amount = amount;
    }
}

// Recommend Stocks Model

class SimpleRecommendModel {
    ArrayList<String> stocksToBuy;

    public SimpleRecommendModel(ArrayList<String> stocksToBuy) {
        this.stocksToBuy = stocksToBuy;
    }
}