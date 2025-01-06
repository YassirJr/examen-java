package com.javaoop.examen.models;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repas {
    private Integer id;
    private Double totalPrix;
    private PlatPrincipal platPrincipal;
    private List<Supplement> supplements;
    private List<Ingredient> ingredients;

    public double calculerTotal() {
        double total = platPrincipal.calculerPrix();
        total += supplements.stream().mapToDouble(Supplement::getPrix).sum();
        total += ingredients.stream().mapToDouble(Ingredient::getPrix).sum();
        return total;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f€)", platPrincipal.getNom(), calculerTotal());
    }

}
