package com.javaoop.examen.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplement {
    private Integer id;
    private String nom;
    private Double prix;

    @Override
    public String toString() {
        return String.format("%s (%.2f€)", getNom(), getPrix());
    }
}
