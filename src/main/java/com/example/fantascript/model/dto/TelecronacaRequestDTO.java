package com.example.fantascript.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelecronacaRequestDTO {
    private String fase;                     // “semifinale” o “finale”
    private String squadraCasa;
    private int [] golCasa;
    private String squadraTrasferta;
    private int [] golTrasferta;
    private List<GiocatoreGoalDTO> marcatori; // lista dei marcatori con minuto
}
