package com.example.fantascript.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SquadraDTO {
    private String nome;
    private String logo;

    //@JsonBackReference // Dichiara che questo lato della relazione è "inverse"
   // private List<PartitaDTO> partite;
}
