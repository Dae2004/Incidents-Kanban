package com.helpdeskflow.persistence;

import com.helpdeskflow.exception.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DEFAULT_URL = "jdbc:sqlite:src/main/resources/database/helpdeskflow.db";
    private static final String CREATE_INCIDENTS_TABLE = """
            CREATE TABLE IF NOT EXISTS incidents (
                incident_id TEXT PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                category TEXT NOT NULL,
                impact TEXT NOT NULL,
                urgency TEXT NOT NULL,
                priority TEXT NOT NULL,
                status TEXT NOT NULL,
                creation_date TEXT NOT NULL,
                closing_date TEXT,
                solution_description TEXT,
                class_of_service TEXT NOT NULL
            )
            """;

    private final String databaseUrl;

    public DatabaseManager() {
        this(DEFAULT_URL);
    }

    public DatabaseManager(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    public void initializeSchema() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_INCIDENTS_TABLE);
        } catch (SQLException exception) {
            throw new PersistenceException("Unable to initialize the incidents schema", exception);
        }
    }
}
