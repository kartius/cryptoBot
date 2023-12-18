package com.kartius.trading.bot.service;

import com.google.gson.Gson;
import com.kartius.trading.bot.model.StrategySettings;
import com.kartius.trading.bot.strategy.Strategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;

@Slf4j
public class TraidingService {
    private final Function<StrategySettings.Settings, Strategy> strategyFactory;
    private StrategySettings strategySettings;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    private final Set<Strategy> strategies = new HashSet<>();

    public TraidingService(Function<StrategySettings.Settings, Strategy> strategyFactory) {
        initStrategiesSettings();
        this.strategyFactory = strategyFactory;
        trade();
    }

    private void initStrategiesSettings() {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("settings.json")) {
            this.strategySettings = new Gson().fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), StrategySettings.class);
            System.out.println(this.strategySettings);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void trade() {
        for (StrategySettings.Settings setting : strategySettings.getSettings()) {
            log.info(" Start " + setting.getStrategy());
            strategies.add(executeStrategy(setting));
        }
        for (Strategy strategy : strategies) {
            strategy.run();
        }
    }

    @SneakyThrows
    private Strategy executeStrategy(StrategySettings.Settings setting) {
        ScheduledFuture<Strategy> scheduledFuture =
                executorService.schedule(() -> {
                            log.info(String.format("Apply strategy [%s] with exchange [%s] and pair [%s]", setting.getStrategy(), setting.getExchangeName(), setting.getPair()));
                            return strategyFactory.apply(setting);
                        },
                        5,
                        TimeUnit.SECONDS);
       return scheduledFuture.get();
    }

}
