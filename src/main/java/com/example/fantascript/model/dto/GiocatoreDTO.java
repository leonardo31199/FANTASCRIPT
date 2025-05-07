package com.example.fantascript.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiocatoreDTO {
    private Long id;
    private String nome;
    private String cognome;
    private String ruolo;
    private int valutazione;
}
