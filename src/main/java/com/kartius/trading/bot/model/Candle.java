package com.kartius.trading.bot.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class Candle {

    /*Opening price of the time interval in quote currency (For BTC/USD, the price would be USD).*/
    private BigDecimal open;
    /*Highest price reached during time interval, in quote currency.*/
    private BigDecimal high;
    /*Lowest price reached during time interval, in quote currency.*/
    private BigDecimal low;
    /*Closing price of the time interval, in the quote currency.*/
    private BigDecimal close;
    /*Quantity of asset bought or sold, displayed in base currency*/
    private BigDecimal volume;
    private Instant time;
}
