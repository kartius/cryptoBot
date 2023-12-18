package com.kartius.trading.bot.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CandleData {

    private List<CandleRaw> candles;

    @Getter
    @Setter
   public class CandleRaw {
        /*Opening price of the time interval in quote currency (For BTC/USD, the price would be USD).*/
        private BigDecimal o;
        /*Highest price reached during time interval, in quote currency.*/
        private BigDecimal h;
        /*Lowest price reached during time interval, in quote currency.*/
        private BigDecimal l;
        /*Closing price of the time interval, in the quote currency.*/
        private BigDecimal c;
        /*Quantity of asset bought or sold, displayed in base currency*/
        private BigDecimal v;
        private long t;
    }
}
