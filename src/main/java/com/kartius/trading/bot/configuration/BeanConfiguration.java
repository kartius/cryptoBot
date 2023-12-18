package com.kartius.trading.bot.configuration;

import com.kartius.trading.bot.exchange.BinanceExchange;
import com.kartius.trading.bot.exchange.Exchange;
import com.kartius.trading.bot.exchange.ExmoExchange;
import com.kartius.trading.bot.model.StrategySettings;
import com.kartius.trading.bot.service.TraidingService;
import com.kartius.trading.bot.strategy.ScalpingStrategy;
import com.kartius.trading.bot.strategy.SimpleStrategy;
import com.kartius.trading.bot.strategy.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@Slf4j
public class BeanConfiguration {

    @Bean
    public TraidingService createRunnerService() {
        return new TraidingService(strategyFactory());
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SimpleStrategy createSimpleStrategy(Exchange exchange, String pair) {
        return new SimpleStrategy(exchange, pair);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ScalpingStrategy createScalpingStrategy() {
        return new ScalpingStrategy();
    }

    @Bean("strategyFactory")
    public Function<StrategySettings.Settings, Strategy> strategyFactory() {
        return this::createStrategy;
    }

    private Strategy createStrategy(StrategySettings.Settings settings) {
        if ("SimpleStrategy".equals(settings.getStrategy())) {
            return createSimpleStrategy(exchanges().get(settings.getExchangeName()), settings.getPair());
        } else if ("ScalpingStrategy".equals(settings.getStrategy())) {
            return createScalpingStrategy();
        } else {
            throw new RuntimeException(String.format("Undefined strategy name [%s]", settings.getStrategy()));
        }
    }

    private Map<String, Exchange> exchanges() {
        Map<String, Exchange> exchanges = new HashMap<>();
        exchanges.put("Exmo", new ExmoExchange());
        exchanges.put("Binance", new BinanceExchange());
        return exchanges;
    }

}
