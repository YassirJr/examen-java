package com.javaoop.examen.dao;

import com.javaoop.examen.database.SingletonConnexionDB;
import com.javaoop.examen.models.Ingredient;
import com.javaoop.examen.models.PlatPrincipal;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatPrincipalDAO implements DAO<PlatPrincipal> {

    private final Connection connection = SingletonConnexionDB.getConnexion();

    @Override
    public List<PlatPrincipal> findAll() {
        List<PlatPrincipal> plats = new ArrayList<>();
        String query = """
            SELECT PP.id AS plat_id, PP.nom AS plat_nom, PP.basePrix AS plat_basePrix, 
                   I.id AS ingredient_id, I.nom AS ingredient_nom, I.prix AS ingredient_prix
            FROM PlatPrincipal PP
            LEFT JOIN Repas R ON PP.id = R.plat_id
            LEFT JOIN RepasIngredient RI ON R.id = RI.repa_id
            LEFT JOIN Ingredient I ON RI.ingredient_id = I.id
        """;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int platId = resultSet.getInt("plat_id");
                PlatPrincipal existingPlat = plats.stream()
                        .filter(plat -> plat.getId() == platId)
                        .findFirst()
                        .orElse(null);

                if (existingPlat == null) {
                    existingPlat = PlatPrincipal.builder()
                            .id(platId)
                            .nom(resultSet.getString("plat_nom"))
                            .basePrix(resultSet.getBigDecimal("plat_basePrix").doubleValue())
                            .ingredients(new ArrayList<>())
                            .build();
                    plats.add(existingPlat);
                }

                int ingredientId = resultSet.getInt("ingredient_id");
                if (ingredientId > 0) {
                    Ingredient ingredient = Ingredient.builder()
                            .id(ingredientId)
                            .nom(resultSet.getString("ingredient_nom"))
                            .prix(resultSet.getBigDecimal("ingredient_prix").doubleValue())
                            .build();
                    existingPlat.getIngredients().add(ingredient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plats;
    }

    @Override
    public PlatPrincipal findById(Integer id) {
        PlatPrincipal plat = null;
        String query = """
            SELECT PP.id AS plat_id, PP.nom AS plat_nom, PP.basePrix AS plat_basePrix, 
                   I.id AS ingredient_id, I.nom AS ingredient_nom, I.prix AS ingredient_prix
            FROM PlatPrincipal PP
            LEFT JOIN Repas R ON PP.id = R.plat_id
            LEFT JOIN RepasIngredient RI ON R.id = RI.repa_id
            LEFT JOIN Ingredient I ON RI.ingredient_id = I.id
            WHERE PP.id = ?
        """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (plat == null) {
                    plat = PlatPrincipal.builder()
                            .id(resultSet.getInt("plat_id"))
                            .nom(resultSet.getString("plat_nom"))
                            .basePrix(resultSet.getBigDecimal("plat_basePrix").doubleValue())
                            .ingredients(new ArrayList<>())
                            .build();
                }

                int ingredientId = resultSet.getInt("ingredient_id");
                if (ingredientId > 0) {
                    Ingredient ingredient = Ingredient.builder()
                            .id(ingredientId)
                            .nom(resultSet.getString("ingredient_nom"))
                            .prix(resultSet.getBigDecimal("ingredient_prix").doubleValue())
                            .build();
                    plat.getIngredients().add(ingredient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plat;
    }

    @Override
    public PlatPrincipal save(PlatPrincipal platPrincipal) {
        String query = "INSERT INTO PlatPrincipal (nom, basePrix) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, platPrincipal.getNom());
            statement.setBigDecimal(2, BigDecimal.valueOf(platPrincipal.getBasePrix()));
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                platPrincipal.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return platPrincipal;
    }

    @Override
    public PlatPrincipal update(Integer id, PlatPrincipal platPrincipal) {
        String query = "UPDATE PlatPrincipal SET nom = ?, basePrix = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, platPrincipal.getNom());
            statement.setBigDecimal(2, BigDecimal.valueOf(platPrincipal.getBasePrix()));
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return platPrincipal;
    }

    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM PlatPrincipal WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
