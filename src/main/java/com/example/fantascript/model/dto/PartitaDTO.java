package com.example.fantascript.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Questo DTO serve per restituire al frontend
// i risultati di una partita simulata
// (nomi delle squadre, gol e vincitore)
public class PartitaDTO {


    private String squadraCasa;
    private int golCasa;

    private String squadraTrasferta;
    private int goltrasferta;

    private String vincitore;

}
