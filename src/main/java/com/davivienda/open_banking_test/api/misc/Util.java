package com.davivienda.open_banking_test.api.misc;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static Map<String, Object> parseQueryString(URI uri) {
        String query = uri.getRawQuery();
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> params = new HashMap<>();

        for (String entry : query.split("&")) {
            int idx = entry.indexOf("=");
            if (idx < 0) {
                params.put(
                        URLDecoder.decode(entry, StandardCharsets.UTF_8),
                        null
                );
            } else {
                params.put(
                        URLDecoder.decode(entry.substring(0, idx), StandardCharsets.UTF_8),
                        URLDecoder.decode(entry.substring(idx + 1), StandardCharsets.UTF_8)
                );
            }
        }

        return params;
    }
}
