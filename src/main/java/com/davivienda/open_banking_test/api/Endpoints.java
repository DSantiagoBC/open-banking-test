package com.davivienda.open_banking_test.api;

import com.davivienda.open_banking_test.api.misc.Endpoint;
import com.davivienda.open_banking_test.api.misc.HttpMethod;
import com.davivienda.open_banking_test.db.SQLiteManager;
import com.davivienda.open_banking_test.models.Producto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Endpoints {
    private final SQLiteManager sql;

    public Endpoints() {
        this.sql = new SQLiteManager("open-banking-test-database.db");
    }

    @Endpoint(method = HttpMethod.GET, path = "/productos/consultar/")
    public List<Producto> consultarProductos(Map<String, Object> params) throws SQLException {
        return this.sql.consultarProductos();
    }
}
