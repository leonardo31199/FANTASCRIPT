package com.example.fantascript.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiocatoreGoalDTO {
    private Long id;
    private String nome;
    private String cognome;
    private int minuto;

}