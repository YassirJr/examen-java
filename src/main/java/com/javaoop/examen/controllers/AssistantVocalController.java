package com.javaoop.examen.controllers;

import com.javaoop.examen.models.AssistantVocal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AssistantVocalController {
    @FXML private Button recordButton;
    @FXML private Label statusLabel;
    @FXML private Label commandesLabel;

    private AssistantVocal assistantVocal;

    @FXML
    public void initialize() {
        assistantVocal = new AssistantVocal();
        recordButton.setOnAction(event -> handleRecordButton());
    }

    private void handleRecordButton() {
        assistantVocal.capturerCommandeAudio();
        String commande = assistantVocal.transcrireAudioTexte();
        assistantVocal.traiterCommande(commande);
        statusLabel.setText("Commande transcrite: " + commande);
        updateCommandesLabel();
    }

    private void updateCommandesLabel() {
        StringBuilder commandesText = new StringBuilder("Commandes:\n");
        for (String commande : assistantVocal.getCommandes()) {
            commandesText.append(commande).append("\n");
        }
        commandesLabel.setText(commandesText.toString());
    }
}