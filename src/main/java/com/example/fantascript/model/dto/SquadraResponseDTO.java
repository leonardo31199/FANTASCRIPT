package com.example.fantascript.model.dto;

import com.example.fantascript.model.entities.Giocatore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SquadraResponseDTO {

    private Long id;
    private String nome;
    private String logo;
    private List<GiocatoreDTO> giocatori;
}
