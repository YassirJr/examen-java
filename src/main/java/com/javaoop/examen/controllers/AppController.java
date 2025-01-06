package com.javaoop.examen.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;


public class AppController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    private void main() {
        mainTabPane.getSelectionModel().select(0);
    }

    @FXML
    private void vocal() {
        mainTabPane.getSelectionModel().select(1);
    }
}