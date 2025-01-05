package com.javaoop.examen.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class MainController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    private void showUsers() {
        mainTabPane.getSelectionModel().select(0);
    }

    @FXML
    private void showDepartements() {
        mainTabPane.getSelectionModel().select(1);
    }
}