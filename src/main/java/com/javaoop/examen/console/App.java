package com.javaoop.examen.console;

import com.javaoop.examen.controllers.MockData;
import com.javaoop.examen.models.Client;
import com.javaoop.examen.models.Repas;


import java.util.List;

public class App {
    public static void main(String[] args) {
        Client client = MockData.getClient();

        List<Repas> repasList = MockData.getRepas();

        String ticket = genererTicket(client, repasList);
        System.out.println(ticket);
    }

    public static String genererTicket(Client client, List<Repas> repas) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bienvenue ").append(client.getNom()).append("\n");
        sb.append("---------TICKET---------\n");
        sb.append("Nom: ").append(client.getNom()).append("\n\n");
        sb.append("Nombre de repas: ").append(repas.size()).append("\n");

        int i = 1;
        for (Repas r : repas) {
            sb.append("Repas N°").append(i).append(":\n");
            sb.append(r.toString()).append("\n");
            i++;
        }

        sb.append("*****\n");
        sb.append("Total: ").append(calculerTotalCommande(repas)).append("\n");
        return sb.toString();
    }

    public static double calculerTotalCommande(List<Repas> repas) {
        return repas.stream().mapToDouble(Repas::getTotalPrix).sum();
    }
}