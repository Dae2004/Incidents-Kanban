package com.helpdeskflow.repository;

import com.helpdeskflow.exception.PersistenceException;
import com.helpdeskflow.model.Category;
import com.helpdeskflow.model.ClassOfService;
import com.helpdeskflow.model.Impact;
import com.helpdeskflow.model.Incident;
import com.helpdeskflow.model.IncidentId;
import com.helpdeskflow.model.Priority;
import com.helpdeskflow.model.Status;
import com.helpdeskflow.model.Urgency;
import com.helpdeskflow.persistence.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** SQLite/JDBC implementation of the incident repository. */
public class IncidentRepositoryJdbc implements IncidentRepository {

    private static final String INSERT_SQL = """
            INSERT INTO incidents (incident_id, title, description, category, impact, urgency,
                priority, status, creation_date, closing_date, solution_description, class_of_service)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String FIND_BY_ID_SQL = "SELECT * FROM incidents WHERE incident_id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM incidents ORDER BY creation_date, incident_id";
    private static final String UPDATE_SQL = """
            UPDATE incidents SET title = ?, description = ?, category = ?, impact = ?, urgency = ?,
                priority = ?, status = ?, creation_date = ?, closing_date = ?,
                solution_description = ?, class_of_service = ?
            WHERE incident_id = ?
            """;
    private static final String DELETE_SQL = "DELETE FROM incidents WHERE incident_id = ?";
    private static final String EXISTS_SQL = "SELECT 1 FROM incidents WHERE incident_id = ?";

    private final DatabaseManager databaseManager;

    public IncidentRepositoryJdbc() {
        this(new DatabaseManager());
    }

    public IncidentRepositoryJdbc(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.databaseManager.initializeSchema();
    }

    @Override
    public void save(Incident incident) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, incident.getId().getValue());
            bindIncidentFields(statement, incident, 2);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new PersistenceException("Unable to save incident", exception);
        }
    }

    @Override
    public Optional<Incident> findById(IncidentId incidentId) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setString(1, incidentId.getValue());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapIncident(resultSet)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Unable to find incident", exception);
        }
    }

    @Override
    public List<Incident> findAll() {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Incident> incidents = new ArrayList<>();
            while (resultSet.next()) {
                incidents.add(mapIncident(resultSet));
            }
            return List.copyOf(incidents);
        } catch (SQLException exception) {
            throw new PersistenceException("Unable to find incidents", exception);
        }
    }

    @Override
    public void update(Incident incident) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            bindUpdate(statement, incident);
            statement.setString(12, incident.getId().getValue());
            if (statement.executeUpdate() == 0) {
                throw new PersistenceException("Incident does not exist: " + incident.getId());
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Unable to update incident", exception);
        }
    }

    @Override
    public void delete(IncidentId incidentId) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setString(1, incidentId.getValue());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new PersistenceException("Unable to delete incident", exception);
        }
    }

    @Override
    public boolean existsById(IncidentId incidentId) {
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_SQL)) {
            statement.setString(1, incidentId.getValue());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Unable to check incident existence", exception);
        }
    }

    private void bindUpdate(PreparedStatement statement, Incident incident) throws SQLException {
        bindIncidentFields(statement, incident, 1);
    }

    private void bindIncidentFields(PreparedStatement statement, Incident incident, int startIndex)
            throws SQLException {
        statement.setString(startIndex, incident.getTitle());
        statement.setString(startIndex + 1, incident.getDescription());
        statement.setString(startIndex + 2, incident.getCategory().name());
        statement.setString(startIndex + 3, incident.getImpact().name());
        statement.setString(startIndex + 4, incident.getUrgency().name());
        statement.setString(startIndex + 5, incident.getPriority().name());
        statement.setString(startIndex + 6, incident.getStatus().name());
        statement.setString(startIndex + 7, incident.getCreationDate().toString());
        setNullableString(statement, startIndex + 8, nullableDate(incident.getClosingDate()));
        setNullableString(statement, startIndex + 9, incident.getSolutionDescription());
        statement.setString(startIndex + 10, incident.getClassOfService().name());
    }

    private Incident mapIncident(ResultSet resultSet) throws SQLException {
        return new Incident(
                new IncidentId(resultSet.getString("incident_id")),
                resultSet.getString("title"),
                resultSet.getString("description"),
                Category.valueOf(resultSet.getString("category")),
                Impact.valueOf(resultSet.getString("impact")),
                Urgency.valueOf(resultSet.getString("urgency")),
                Priority.valueOf(resultSet.getString("priority")),
                Status.valueOf(resultSet.getString("status")),
                LocalDateTime.parse(resultSet.getString("creation_date")),
                parseDate(resultSet.getString("closing_date")),
                resultSet.getString("solution_description"),
                ClassOfService.valueOf(resultSet.getString("class_of_service"))
        );
    }

    private void setNullableString(PreparedStatement statement, int index, String value) throws SQLException {
        if (value == null) {
            statement.setNull(index, java.sql.Types.VARCHAR);
        } else {
            statement.setString(index, value);
        }
    }

    private String nullableDate(LocalDateTime date) {
        return date == null ? null : date.toString();
    }

    private LocalDateTime parseDate(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }
}
