package com.javaoop.examen.controllers;


import com.javaoop.examen.models.Repas;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TicketController {
    @FXML private TextArea ticketTextArea;

    public void setRepas(Repas repas) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("=== TICKET DE COMMANDE ===\n\n");
        ticket.append("Plat Principal: ").append(repas.getPlatPrincipal().getNom()).append("\n");

        ticket.append("\nIngrédients:\n");
        repas.getIngredients().forEach(i ->
                ticket.append("- ").append(i.getNom()).append(" (").append(i.getPrix()).append("€)\n"));

        ticket.append("\nSuppléments:\n");
        repas.getSupplements().forEach(s ->
                ticket.append("- ").append(s.getNom()).append(" (").append(s.getPrix()).append("€)\n"));

        ticket.append("\nTotal: ").append(String.format("%.2f €", repas.calculerTotal()));

        ticketTextArea.setText(ticket.toString());
    }
}
