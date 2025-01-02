package com.davivienda.open_banking_test.db;

import com.davivienda.open_banking_test.models.Producto;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SQLiteManager extends DataBaseManager {
    private final String connectionString;

    public SQLiteManager(String dbName) {
        this.connectionString = "jdbc:sqlite:" + dbName;
        initialize(dbName);
    }

    public void initialize(String dbLocation) {
        File dbFile = new File(dbLocation);
        if (dbFile.isFile())
            return;

        try {
            String ddl = getDDL();
            try (Connection conn = DriverManager.getConnection(this.connectionString);
                 Statement stmt = conn.createStatement()) {
                for (String sql : ddl.split(";")) {
                    stmt.execute(sql);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }

    private String getDDL() throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("ddl.sql");
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
             BufferedReader br = new BufferedReader(isr)) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public List<Producto> consultarProductos() throws SQLException {
        String query = "SELECT * FROM Productos";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(this.connectionString);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getNString("Nombre"),
                        rs.getNString("Descripcion"),
                        rs.getFloat("Precio"),
                        rs.getInt("Cantidad")
                ));
            }
        }

        return productos;
    }
}
