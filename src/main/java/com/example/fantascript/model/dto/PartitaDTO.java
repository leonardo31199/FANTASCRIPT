package com.example.fantascript.model.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id"
)
// Questo DTO serve per restituire al frontend
// i risultati di una partita simulata
// (nomi delle squadre, gol e vincitore)
//

public class PartitaDTO {


    private String squadraCasa;
    private int [] golCasa;

    private String squadraTrasferta;
    private int [] golTrasferta;

    private String vincitore;
    private List<TelecronacaDTO> telecronaca;
//    @JsonBackReference // Dichiara che questo lato della relazione è "inverse"
//    private List<PartitaDTO> partite;
}
