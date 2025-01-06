package com.javaoop.examen.models;

import java.util.ArrayList;
import java.util.List;

public class AssistantVocal {
    private final List<String> commandes;

    public AssistantVocal() {
        this.commandes = new ArrayList<>();
    }

    // Simulates audio recording
    public void capturerCommandeAudio() {
        System.out.println("Enregistrement audio en cours...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Enregistrement terminé.");
    }

    // Simulates audio transcription
    public String transcrireAudioTexte() {
        // Simulate transcription
        return "Ajouter 2 plats de pâtes";
    }

    public void traiterCommande(String commande) {
        commandes.add(commande);
        System.out.println("Commande ajoutée: " + commande);
    }

    public List<String> getCommandes() {
        return commandes;
    }
}