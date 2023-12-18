package com.kartius.trading.bot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Getter
@Setter
public class ApplicationConfiguration {
    @Value("${pairs}")
    private List<String> pairs;

}
