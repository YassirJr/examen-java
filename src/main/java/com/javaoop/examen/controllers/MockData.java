// src/main/java/com/javaoop/examen/controllers/MockData.java
package com.javaoop.examen.controllers;

import com.javaoop.examen.dao.ClientDAO;
import com.javaoop.examen.dao.IngredientDAO;
import com.javaoop.examen.dao.PlatPrincipalDAO;
import com.javaoop.examen.dao.SupplementDAO;
import com.javaoop.examen.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

public class MockData {
    private static final ClientDAO clientDAO = new ClientDAO();
    private static final IngredientDAO ingredientDAO = new IngredientDAO();
    private static final PlatPrincipalDAO platPrincipalDAO = new PlatPrincipalDAO();
    private static final SupplementDAO supplementDAO = new SupplementDAO();

    public static ObservableList<PlatPrincipal> getPlats() {
        return FXCollections.observableArrayList(
                platPrincipalDAO.findAll()
        );
    }

    public static ObservableList<Ingredient> getIngredients() {
        return FXCollections.observableArrayList(
                ingredientDAO.findAll()
        );
    }

    public static ObservableList<Supplement> getSupplements() {
        return FXCollections.observableArrayList(
                supplementDAO.findAll()
        );
    }

    public static ObservableList<Repas> getRepas() {
        return FXCollections.observableArrayList(
                Repas.builder()
                        .id(1)
                        .totalPrix(10.0)
                        .platPrincipal(PlatPrincipal.builder()
                                .id(1)
                                .nom("Pizza Margherita")
                                .basePrix(10.0)
                                .ingredients(new ArrayList<>())
                                .build())
                        .supplements(new ArrayList<>())
                        .ingredients(new ArrayList<>())
                        .build()
        );
    }

    public static Client getClient() {
        return clientDAO.findById(1);
    }

    public static void makeCommand(Command command) {
        clientDAO.addCommand(getClient().getId()  , command);

    }
}