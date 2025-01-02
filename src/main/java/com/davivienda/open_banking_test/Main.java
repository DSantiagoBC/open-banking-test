package com.davivienda.open_banking_test;

import com.davivienda.open_banking_test.api.Endpoints;
import com.davivienda.open_banking_test.api.misc.Endpoint;
import com.davivienda.open_banking_test.api.misc.Util;
import com.fasterxml.jackson.jr.ob.JSON;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 3001;
        Endpoints endpoints = new Endpoints();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        for (Method method : Endpoints.class.getMethods()) {
            method.setAccessible(true);
            Endpoint endpoint = method.getAnnotation(Endpoint.class);
            if (endpoint == null)
                continue;

            server.createContext(endpoint.path(), (HttpExchange exchange) -> {
                Map<String, Object> parameters;
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        parameters = Util.parseQueryString(exchange.getRequestURI());
                        break;
                    case "POST":
                        try (InputStream is = exchange.getRequestBody()) {
                            parameters = JSON.std.mapFrom(is);
                        }
                        break;
                    default:
                        exchange.sendResponseHeaders(405, -1);
                        exchange.close();
                        return;
                }

                Object result = null;
                try {
                    result = method.invoke(endpoints, parameters);
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    exchange.sendResponseHeaders(405, -1);
                } catch (Exception e) {
                    exchange.sendResponseHeaders(500, -1);
                }

                if (result == null) {
                    exchange.sendResponseHeaders(200, -1);
                } else {
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    byte[] bytes = JSON.std
                            .without(JSON.Feature.PRETTY_PRINT_OUTPUT)
                            .with(JSON.Feature.WRITE_NULL_PROPERTIES)
                            .asBytes(result);
                    exchange.sendResponseHeaders(200, bytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(bytes);
                    }
                }

                exchange.close();
            });
//          context.setAuthenticator(new Authenticator() {
//              @Override
//              public Result authenticate(HttpExchange exch) {
//                  return null;
//              }
//          })
        }

        server.setExecutor(null);
        server.start();
    }
}
