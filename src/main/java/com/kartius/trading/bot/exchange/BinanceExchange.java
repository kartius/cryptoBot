package com.kartius.trading.bot.exchange;

import com.kartius.trading.bot.model.Candle;
import com.kartius.trading.bot.model.Order;
import com.kartius.trading.bot.model.Ticker;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class BinanceExchange implements Exchange {
    @Override
    public void initConnection() {

    }

    @Override
    public Map<String, Ticker> getTickers(String pair) {
        return null;
    }

    @Override
    public Map<String, List<Candle>> getCandles(String currencyPair, Instant from, Instant to) {
        return null;
    }

    @Override
    public Map<String, Order> getOpenedOrders() {
        return null;
    }

    @Override
    public void createOrder(String orderType, double price, String pair, double quantity) {

    }

    @Override
    public void cancelOrder(String OrderId) {

    }
}
