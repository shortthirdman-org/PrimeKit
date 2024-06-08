package com.shortthirdman.primekit.essentials.common.util;

import org.apache.commons.lang3.StringUtils;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc.PgConnection;

import java.sql.SQLException;

public class PostgreSQLUtils {

    public static void createNewSchema(String schemaName) {
        if (schemaName == null || StringUtils.isBlank(schemaName)) {
            throw new IllegalArgumentException("Schema name is null. Unable to proceed for schema creation");
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(System.getenv("PG_HOST"));
        dataSource.setUser(System.getenv("PG_USER"));
        dataSource.setPassword(System.getenv("PG_PASSWORD"));

        try (PgConnection connection = (PgConnection) dataSource.getConnection()) {
            // Get a connection
            var schemas = connection.getMetaData().getSchemas();

            // Check if the schema exists
            boolean schemaExists = false;

            if (schemas.next()) {
                // schemas
                // contains(schemaName)
                schemaExists = true;
            }

            if (schemaExists) {
                System.out.println("Schema exists.");
            } else {
                System.out.println("Schema does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
