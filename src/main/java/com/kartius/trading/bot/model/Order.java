package com.kartius.trading.bot.model;

import java.math.BigDecimal;

public class Order {

    private long created;
    private long order_id;
    private String type;
    private String pair;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
}
