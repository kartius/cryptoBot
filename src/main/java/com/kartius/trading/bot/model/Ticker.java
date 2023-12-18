package com.kartius.trading.bot.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class Ticker {

    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal awgPrice;
    private BigDecimal lastTrade;
    private Instant updated;
}
