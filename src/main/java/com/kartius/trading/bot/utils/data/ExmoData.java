package com.kartius.trading.bot.utils.data;

import com.kartius.trading.bot.model.Candle;
import com.kartius.trading.bot.model.Ticker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExmoData {

    public static Map<String, Ticker> TICKERS = new HashMap<>();
    public static Map<String, List<Candle>> CANDLES_MAP = new HashMap<>();
}
