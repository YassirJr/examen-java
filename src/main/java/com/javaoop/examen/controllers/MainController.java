// src/main/java/com/javaoop/examen/controllers/MainController.java
package com.javaoop.examen.controllers;

import com.javaoop.examen.models.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private ListView<PlatPrincipal> platsListView;
    @FXML private ListView<Ingredient> ingredientsListView;
    @FXML private ListView<Supplement> supplementsListView;
    @FXML private Label totalLabel;
    @FXML private Button commandButton;

    private final ObservableList<PlatPrincipal> selectedPlats = FXCollections.observableArrayList();
    private final ObservableList<Ingredient> selectedIngredients = FXCollections.observableArrayList();
    private final ObservableList<Supplement> selectedSupplements = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupPlatsListView();
        setupIngredientsListView();
        setupSupplementsListView();
        updateTotal();

        commandButton.setOnAction(event -> passCommand());
    }

    private void setupPlatsListView() {
        platsListView.setItems(MockData.getPlats());
        platsListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        updateIngredientsView(newVal);
                        updateTotal();
                    }
                }
        );
    }

    private void setupIngredientsListView() {
        ingredientsListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedIngredients.add(item);
                } else {
                    selectedIngredients.remove(item);
                }
                updateTotal();
            });
            return observable;
        }));
    }

    private void setupSupplementsListView() {
        supplementsListView.setItems(MockData.getSupplements());
        supplementsListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedSupplements.add(item);
                } else {
                    selectedSupplements.remove(item);
                }
                updateTotal();
            });
            return observable;
        }));
    }

    private void updateIngredientsView(PlatPrincipal plat) {
        selectedIngredients.clear();

        ObservableList<Ingredient> availableIngredients = MockData.getIngredients();

        ingredientsListView.setItems(availableIngredients);

        ingredientsListView.getItems().forEach(ingredient -> {
            BooleanProperty observable = new SimpleBooleanProperty(false);
            CheckBoxListCell<Ingredient> cell = (CheckBoxListCell<Ingredient>)
                    ingredientsListView.lookup("#" + ingredient.getId());
            if (cell != null) {
                observable.set(false);
            }
        });
    }

    private void updateTotal() {
        double total = calculateTotal();
        totalLabel.setText(String.format("Total: %.2f €", total));
    }

    private double calculateTotal() {
        double total = 0;
        PlatPrincipal selectedPlat = platsListView.getSelectionModel().getSelectedItem();
        if (selectedPlat != null) {
            total += selectedPlat.getBasePrix();
            total += selectedIngredients.stream().mapToDouble(Ingredient::getPrix).sum();
            total += selectedSupplements.stream().mapToDouble(Supplement::getPrix).sum();
        }
        return total;
    }

    @FXML
    private void handleConfirmOrder() {
        PlatPrincipal selectedPlat = platsListView.getSelectionModel().getSelectedItem();
        if (selectedPlat == null) {
            showAlert("Erreur", "Veuillez sélectionner un plat principal.");
            return;
        }

        Repas repas = Repas.builder()
                .platPrincipal(selectedPlat)
                .ingredients(new ArrayList<>(selectedIngredients))
                .supplements(new ArrayList<>(selectedSupplements))
                .totalPrix(calculateTotal())
                .build();

        showTicket();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearSelections() {
        platsListView.getSelectionModel().clearSelection();
        selectedIngredients.clear();
        selectedSupplements.clear();
        ingredientsListView.getItems().clear();
        supplementsListView.getItems().forEach(supplement -> {
            BooleanProperty observable = new SimpleBooleanProperty(false);
            CheckBoxListCell<Supplement> cell = (CheckBoxListCell<Supplement>)
                    supplementsListView.lookup("#" + supplement.getId());
            if (cell != null) {
                observable.set(false);
            }
        });
        updateTotal();
    }

    private void showTicket() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ticket de Commande");
        dialog.setHeaderText("Récapitulatif de votre commande");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        PlatPrincipal selectedPlat = platsListView.getSelectionModel().getSelectedItem();
        if (selectedPlat != null) {
            Label platLabel = new Label("Plat Principal:");
            platLabel.setStyle("-fx-font-weight: bold");
            content.getChildren().add(platLabel);
            content.getChildren().add(new Label(String.format("%s (%.2f€)",
                    selectedPlat.getNom(),
                    selectedPlat.getBasePrix())));
        }

        if (!selectedIngredients.isEmpty()) {
            Label ingredientsLabel = new Label("\nIngrédients Supplémentaires:");
            ingredientsLabel.setStyle("-fx-font-weight: bold");
            content.getChildren().add(ingredientsLabel);
            selectedIngredients.forEach(ingredient ->
                    content.getChildren().add(new Label(String.format("- %s (%.2f€)",
                            ingredient.getNom(), ingredient.getPrix())))
            );
        }

        if (!selectedSupplements.isEmpty()) {
            Label supplementsLabel = new Label("\nSuppléments:");
            supplementsLabel.setStyle("-fx-font-weight: bold");
            content.getChildren().add(supplementsLabel);
            selectedSupplements.forEach(supplement ->
                    content.getChildren().add(new Label(String.format("- %s (%.2f€)",
                            supplement.getNom(), supplement.getPrix())))
            );
        }

        double total = calculateTotal();
        Label totalLabel = new Label(String.format("\nTotal: %.2f€", total));
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px");
        content.getChildren().add(totalLabel);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> clearSelections());
    }


    private void passCommand() {
        PlatPrincipal selectedPlat = platsListView.getSelectionModel().getSelectedItem();
        if (selectedPlat == null) {
            showAlert("Erreur", "Veuillez sélectionner un plat principal.");
            return;
        }

        List<Repas> repasList = MockData.getRepas();

        Command commande = Command.builder()
                .client_id(MockData.getClient().getId()) 
                .date(new java.util.Date())
                .repas(repasList)
                .total(repasList.stream().reduce(0.0, (total, repas) -> total + repas.getTotalPrix(), Double::sum))
                .build();

        MockData.makeCommand(commande);
        showTicket();
        clearSelections();
    }
}
