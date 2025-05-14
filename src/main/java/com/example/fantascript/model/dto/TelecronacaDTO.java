package com.example.fantascript.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelecronacaDTO {
    private int minuto;
    private String commento;
    private int x;
    private int y;
}