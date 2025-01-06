package com.javaoop.examen.dao;

import com.javaoop.examen.database.SingletonConnexionDB;
import com.javaoop.examen.models.Client;
import com.javaoop.examen.models.Command;
import com.javaoop.examen.models.Repas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements DAO<Client> {
    private final Connection connection = SingletonConnexionDB.getConnexion();

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM Client";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                clients.add(new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("telephone"),
                        null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Client findById(Integer id) {
        Client client = null;
        String query = "SELECT * FROM Client WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("telephone"),
                        getClientCommands(id)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Client save(Client client) {
        String query = "INSERT INTO Client (nom, prenom, email, telephone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getNom());
            statement.setString(2, client.getPrenom());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getTelephone());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                client.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Client update(Integer id, Client client) {
        String query = "UPDATE Client SET nom = ?, prenom = ?, email = ?, telephone = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getNom());
            statement.setString(2, client.getPrenom());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getTelephone());
            statement.setInt(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM Client WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCommand(Integer clientId, Command command) {
        String insertCommand = "INSERT INTO Commande (client_id, total) VALUES (?, ?)";
        String linkRepas = "INSERT INTO CommandeRepas (commande_id, repa_id) VALUES (?, ?)";
        try (PreparedStatement commandStmt = connection.prepareStatement(insertCommand, Statement.RETURN_GENERATED_KEYS)) {
            commandStmt.setInt(1, clientId);
            commandStmt.setDouble(2, command.calculerTotal());
            commandStmt.executeUpdate();

            ResultSet keys = commandStmt.getGeneratedKeys();
            if (keys.next()) {
                command.setId(keys.getInt(1));
                try (PreparedStatement repasStmt = connection.prepareStatement(linkRepas)) {
                    for (Repas repas : command.getRepas()) {
                        repasStmt.setInt(1, command.getId());
                        repasStmt.setInt(2, repas.getId());
                        repasStmt.addBatch();
                    }
                    repasStmt.executeBatch();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Command> getClientCommands(Integer clientId) {
        List<Command> commands = new ArrayList<>();
        String query = """
                SELECT C.id AS commande_id, C.date, C.total, R.id AS repas_id, R.totalPrix AS repasPrix
                FROM Commande C
                JOIN CommandeRepas CR ON C.id = CR.commande_id
                JOIN Repas R ON CR.repa_id = R.id
                WHERE C.client_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int commandId = resultSet.getInt("commande_id");
                Command command = commands.stream().filter(c -> c.getId() == commandId).findFirst().orElse(null);
                if (command == null) {
                    command = Command.builder()
                            .id(commandId)
                            .date(resultSet.getDate("date"))
                            .total(resultSet.getDouble("total"))
                            .repas(new ArrayList<>())
                            .build();
                    commands.add(command);
                }
                command.getRepas().add(Repas.builder()
                        .id(resultSet.getInt("repas_id"))
                        .totalPrix(resultSet.getDouble("repasPrix"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }
}
