package com.kartius.trading.bot.exchange;

import com.kartius.trading.bot.model.Candle;
import com.kartius.trading.bot.model.Order;
import com.kartius.trading.bot.model.Ticker;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface Exchange {

    void initConnection();

    Map<String, Ticker> getTickers(String pair) throws IOException;

    Map<String, List<Candle>> getCandles(String currencyPair, Instant from, Instant to) throws IOException;

    Map<String, Order> getOpenedOrders();

    void createOrder(String orderType, double price, String pair, double quantity) throws IOException;

    void cancelOrder(String OrderId);
}
