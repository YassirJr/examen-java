package com.javaoop.examen.dao;

import com.javaoop.examen.database.SingletonConnexionDB;
import com.javaoop.examen.models.Supplement;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplementDAO implements DAO<Supplement> {

    private final Connection connection = SingletonConnexionDB.getConnexion();

    @Override
    public List<Supplement> findAll() {
        List<Supplement> supplements = new ArrayList<>();
        String query = "SELECT * FROM Supplement";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                supplements.add(Supplement.builder()
                        .id(resultSet.getInt("id"))
                        .nom(resultSet.getString("nom"))
                        .prix(resultSet.getBigDecimal("prix").doubleValue())
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return supplements;
    }

    @Override
    public Supplement findById(Integer id) {
        Supplement supplement = null;
        String query = "SELECT * FROM Supplement WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                supplement = Supplement.builder()
                        .id(resultSet.getInt("id"))
                        .nom(resultSet.getString("nom"))
                        .prix(resultSet.getBigDecimal("prix").doubleValue())
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return supplement;
    }

    @Override
    public Supplement save(Supplement supplement) {
        String query = "INSERT INTO Supplement (nom, prix) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, supplement.getNom());
            statement.setBigDecimal(2, BigDecimal.valueOf(supplement.getPrix()));
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                supplement.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return supplement;
    }

    @Override
    public Supplement update(Integer id, Supplement supplement) {
        String query = "UPDATE Supplement SET nom = ?, prix = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, supplement.getNom());
            statement.setBigDecimal(2, BigDecimal.valueOf(supplement.getPrix()));
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return supplement;
    }

    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM Supplement WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
