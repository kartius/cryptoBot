package com.kartius.trading.bot.authorization;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        ExmoAuthRequest e = new ExmoAuthRequest();
        String result = e.makeRequest("user_open_orders", null);
        System.out.println(result);
        String result2 = e.makeRequest("user_cancelled_orders", new HashMap<String, String>() {{
            put("limit", "2");
            put("offset", "0");
        }});
        System.out.println(result2);
    }
}
