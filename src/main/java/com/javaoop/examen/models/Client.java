package com.javaoop.examen.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private List<Command> commands;

}
