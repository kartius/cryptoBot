package com.kartius.trading.bot.strategy;

import java.io.IOException;

public interface Strategy {

    void run() throws IOException, InterruptedException;
}
