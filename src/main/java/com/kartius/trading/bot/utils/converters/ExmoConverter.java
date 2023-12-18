package com.kartius.trading.bot.utils.converters;

import com.google.gson.internal.LinkedTreeMap;
import com.kartius.trading.bot.model.Candle;
import com.kartius.trading.bot.model.CandleData;
import com.kartius.trading.bot.model.Ticker;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.kartius.trading.bot.utils.data.ExmoData.CANDLES_MAP;
import static com.kartius.trading.bot.utils.data.ExmoData.TICKERS;

public class ExmoConverter {

    public static void convertTicker(String pair, Map<String, LinkedTreeMap> sourceTickers) {
        LinkedTreeMap linkedTreeMap = sourceTickers.get(pair);
        Ticker ticker = new Ticker();
        ticker.setBuyPrice(new BigDecimal((String) linkedTreeMap.get("buy_price")));
        ticker.setSellPrice(new BigDecimal((String) linkedTreeMap.get("sell_price")));
        ticker.setHighPrice(new BigDecimal((String) linkedTreeMap.get("high")));
        ticker.setLowPrice(new BigDecimal((String) linkedTreeMap.get("low")));
        ticker.setAwgPrice(new BigDecimal((String) linkedTreeMap.get("avg")));
        ticker.setLastTrade(new BigDecimal((String) linkedTreeMap.get("last_trade")));
        TICKERS.put(pair, ticker);
    }

    public static void convertCandles(String pair, CandleData.CandleRaw candleRaw, List<Candle> candles) {
        Candle candle = new Candle();
        candle.setClose(candleRaw.getC());
        candle.setOpen(candleRaw.getO());
        candle.setHigh(candleRaw.getH());
        candle.setLow(candleRaw.getL());
        candle.setVolume(candleRaw.getV());
        candle.setTime(Instant.ofEpochSecond(candleRaw.getT()));
        candles.add(candle);
        CANDLES_MAP.put(pair, candles);
    }
}
