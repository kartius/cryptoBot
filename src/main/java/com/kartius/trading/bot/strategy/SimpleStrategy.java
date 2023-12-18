package com.kartius.trading.bot.strategy;

import com.kartius.trading.bot.configuration.ApplicationConfiguration;
import com.kartius.trading.bot.exchange.Exchange;
import com.kartius.trading.bot.model.Candle;
import com.kartius.trading.bot.model.Ticker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

@Slf4j
public class SimpleStrategy implements Strategy {

    @Autowired
    private ApplicationConfiguration configuration;

    private final Exchange exchange;
    private final String pair;
    private static boolean isBuy = true;
    private static double buyPrice = 100;

    public SimpleStrategy(Exchange exchange, String pair) {
        this.exchange = exchange;
        this.pair = pair;
    }

    @SneakyThrows
    @Override
//    @Scheduled(fixedRate = 1000 * 60)
    public void run() throws IOException {
        while (true) {
            Thread.sleep(3000);
//        final Map<String, Order> openedOrders = exchange.getOpenedOrders();
//        exchange.createOrder("buy", 12500, "BTC_USD", 0.00042568);
            final Instant to = Instant.now();
            final Instant from = to.minus(Duration.ofMinutes(60));
            final Map<String, List<Candle>> candlesMap = exchange.getCandles(this.pair, from, to);
            final List<Candle> candles = candlesMap.get(this.pair);
            MathContext mc = new MathContext(7);

            BigDecimal candlesSum = new BigDecimal("0.0", mc);
            double candlesSumD = 0;
            for (Candle candle : candles) {
                candlesSum = candlesSum.add(BigDecimal.valueOf(candle.getClose().doubleValue()), mc);
                candlesSumD += candle.getClose().doubleValue();
            }

            //double SMA = candlesSum.divide(new BigDecimal(candles.size()), mc).doubleValue();
            double SMA = candlesSumD / candles.size();

//        BigDecimal sumSubStructs = new BigDecimal(0);
            float sumSubStructs = 0;

            for (Candle candle : candles) {
                double subtract = candle.getClose().doubleValue() - SMA;
                subtract = subtract * subtract;
                sumSubStructs += subtract;
//            BigDecimal subtract = candle.getClose().subtract(new BigDecimal(SMA));
//            subtract = subtract.multiply(subtract);
//            sumSubStructs.add(subtract);
            }

//        double STD_DEV = Math.sqrt(sumSubStructs.divide(new BigDecimal(candles.size() - 1)).doubleValue());
//        new BigDecimal(sumSubStructs).divide(new BigDecimal(candles.size() - 1)).doubleValue();
            double STD_DEV =
                    Math.sqrt(new BigDecimal(sumSubStructs, mc)
                            .divide(new BigDecimal(candles.size() - 1, mc), RoundingMode.HALF_UP)
                            .doubleValue());

            double TL = SMA + (STD_DEV * 2);

            double ML = SMA - (STD_DEV * 2);
            log.info("STD_DEV = " + STD_DEV);
            log.info("TL = " + TL);
            log.info("ML = " + ML);

//        for (int i = 0; i < 55; i++) {
//            sleep(1000);

            for (Map.Entry<String, Ticker> stringTickerEntry : exchange.getTickers(this.pair).entrySet()) {
                final double lastTrade = stringTickerEntry.getValue().getLastTrade().doubleValue();
                log.info("Last trade - " + lastTrade);
                if (isBuy) {
                    log.info("Now trying to buy...");
                } else {
                    log.info("Now trying to sell...");
                }
                //0.19 - 1.5 percentage is profit and 0.4 is commission
                if (!isBuy && (lastTrade > buyPrice + (buyPrice * 0.019))) {
//                        exchange.createOrder("sell", lastTrade, "BTC_USD", 0.0001);
                    log.info("SELL LTC with price - " + lastTrade + "and TL = " + TL);
                    isBuy = true;

                } else if (lastTrade <= ML && isBuy) {
//                        exchange.createOrder("buy", lastTrade, "BTC_USD", 0.0001);
                    buyPrice = lastTrade;
                    log.info("BUY LTC with price - " + buyPrice + "and TL = " + TL);
                    isBuy = false;
                }
            }
        }
//        }
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        SimpleStrategy simpleStrategy = new SimpleStrategy(new ExmoExchange());
//        simpleStrategy.run();
//    }
}
