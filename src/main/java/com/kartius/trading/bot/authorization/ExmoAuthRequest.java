package com.kartius.trading.bot.authorization;

import com.kartius.trading.bot.configuration.ApplicationConfiguration;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExmoAuthRequest {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    private static long _nonce;
    @Value("${EXMO_KEY}")
    private String _key;
    @Value("${EXMO_SECRET}")
    private String _secret;

    public final String makeRequest(String method, Map<String, String> arguments) {
        _nonce = System.nanoTime();
        Mac mac;
        SecretKeySpec key;
        String sign;

        if (arguments == null) {  // If the user provided no arguments, just create an empty argument array.
            arguments = new HashMap<>();
        }

        arguments.put("nonce", "" + ++_nonce);  // Add the dummy nonce.

        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String, String> stringStringEntry : arguments.entrySet()) {
            Map.Entry argument = stringStringEntry;

            if (postData.length() > 0) {
                postData.append("&");
            }
            postData.append(argument.getKey()).append("=").append(argument.getValue());
        }

        // Create a new secret key
        try {
            key = new SecretKeySpec(_secret.getBytes("UTF-8"), "HmacSHA512");
        } catch (UnsupportedEncodingException uee) {
            System.err.println("Unsupported encoding exception: " + uee.toString());
            return null;
        }

        // Create a new mac
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("No such algorithm exception: " + nsae.toString());
            return null;
        }

        // Init mac with key.
        try {
            mac.init(key);
        } catch (InvalidKeyException ike) {
            System.err.println("Invalid key exception: " + ike.toString());
            return null;
        }


        // Encode the post data by the secret and encode the result as base64.
        try {
            sign = Hex.encodeHexString(mac.doFinal(postData.toString().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException uee) {
            System.err.println("Unsupported encoding exception: " + uee.toString());
            return null;
        }

        // Now do the actual request
        MediaType form = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        try {

            RequestBody body = RequestBody.create(form, postData.toString());
            Request request = new Request.Builder()
                    .url("https://api.exmo.com/v1.1/" + method)
                    .addHeader("Key", _key)
                    .addHeader("Sign", sign)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            System.err.println("Request fail: " + e.toString());
            return null;  // An error occured...
        }
    }
}
