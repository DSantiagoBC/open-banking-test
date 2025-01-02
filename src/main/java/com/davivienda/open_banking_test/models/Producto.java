package com.davivienda.open_banking_test.models;

public record Producto(
        String nombre,
        String descripcion,
        float precio,
        int cantidad
) {}
