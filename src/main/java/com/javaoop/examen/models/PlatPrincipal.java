package com.javaoop.examen.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatPrincipal {
    private Integer id;
    private String nom;
    private Double basePrix;
    private List<Ingredient> ingredients;

    public double calculerPrix() {
        return ingredients.stream().mapToDouble(Ingredient::getPrix).sum();
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f€)", getNom(), getBasePrix());
    }
}
