package com.kartius.trading.bot.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StrategySettings {
    private List<Settings> settings;

    @Getter
    @Setter
   public class Settings {
        private String exchangeName;
        private String pair;
        private String strategy;
    }

    @Override
    public String toString() {
        return "StrategySettings{" +
                "settings=" + settings +
                '}';
    }
}
