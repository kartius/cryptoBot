package com.kartius.trading.bot.exchange;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kartius.trading.bot.authorization.ExmoAuthRequest;
import com.kartius.trading.bot.model.Candle;
import com.kartius.trading.bot.model.CandleData;
import com.kartius.trading.bot.model.Order;
import com.kartius.trading.bot.model.Ticker;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kartius.trading.bot.utils.converters.ExmoConverter.convertCandles;
import static com.kartius.trading.bot.utils.converters.ExmoConverter.convertTicker;
import static com.kartius.trading.bot.utils.data.ExmoData.CANDLES_MAP;
import static com.kartius.trading.bot.utils.data.ExmoData.TICKERS;

@Component
public class ExmoExchange implements Exchange {

    @Autowired
    private ExmoAuthRequest exmoAuthRequest;

    @Override
    public void initConnection() {

    }

    @Override
    public Map<String, Ticker> getTickers(String pair) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.exmo.com/v1.1/ticker")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();

        Map<String, LinkedTreeMap> sourceTickers = new Gson().fromJson(response.body().string(), Map.class);
        convertTicker(pair, sourceTickers);
        return TICKERS;
    }

    @Override
    public Map<String, List<Candle>> getCandles(String currencyPair, Instant from, Instant to) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("https://api.exmo.com/v1.1/candles_history?symbol=%s&resolution=5&from=%d&to=%d",
                        currencyPair, from.getEpochSecond(), to.getEpochSecond()))
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        final CandleData candleData = new Gson().fromJson(response.body().string(), CandleData.class);
        List<Candle> candles = new ArrayList<>();
        candleData.getCandles().stream().forEach(c -> convertCandles(currencyPair, c, candles));
        return CANDLES_MAP;
    }

    @Override
    public Map<String, Order> getOpenedOrders() {
        final String response = exmoAuthRequest.makeRequest("user_open_orders", null);
        return new Gson().fromJson(response, Map.class);

    }

    @Override
    public void createOrder(String orderType, double price, String pair, double quantity) {
        final HashMap<String, String> attributes = new HashMap<>();
        attributes.put("type", orderType);
        attributes.put("pair", pair);
        attributes.put("quantity", String.valueOf(quantity));
        attributes.put("price", String.valueOf(price));
        final String order_create = exmoAuthRequest.makeRequest("order_create", attributes);

        System.out.println(order_create);
    }

    @Override
    public void cancelOrder(String OrderId) {

    }
}
