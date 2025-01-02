package com.davivienda.open_banking_test.api.misc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
    HttpMethod method();

    String path();
}
