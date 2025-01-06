package com.javaoop.examen.models;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Command {
    private Integer id;
    private Date date;
    private Integer client_id;
    private Double total;
    private List<Repas> repas = new ArrayList<>();

    public double calculerTotal() {
        return repas.stream().mapToDouble(Repas::calculerTotal).sum();
    }
}
