package com.javaoop.examen.dao;

import com.javaoop.examen.database.SingletonConnexionDB;
import com.javaoop.examen.models.Ingredient;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO implements DAO<Ingredient> {

    private final Connection connection = SingletonConnexionDB.getConnexion();

    @Override
    public List<Ingredient> findAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT * FROM Ingredient";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                ingredients.add(Ingredient.builder()
                        .id(resultSet.getInt("id"))
                        .nom(resultSet.getString("nom"))
                        .prix(resultSet.getBigDecimal("prix").doubleValue())
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    @Override
    public Ingredient findById(Integer id) {
        Ingredient ingredient = null;
        String query = "SELECT * FROM Ingredient WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ingredient = Ingredient.builder()
                        .id(resultSet.getInt("id"))
                        .nom(resultSet.getString("nom"))
                        .prix(resultSet.getBigDecimal("prix").doubleValue())
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredient;
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        String query = "INSERT INTO Ingredient (nom, prix) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ingredient.getNom());
            statement.setBigDecimal(2, BigDecimal.valueOf(ingredient.getPrix()));
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                ingredient.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredient;
    }

    @Override
    public Ingredient update(Integer id, Ingredient ingredient) {
        String query = "UPDATE Ingredient SET nom = ?, prix = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ingredient.getNom());
            statement.setBigDecimal(2, BigDecimal.valueOf(ingredient.getPrix()));
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredient;
    }

    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM Ingredient WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
