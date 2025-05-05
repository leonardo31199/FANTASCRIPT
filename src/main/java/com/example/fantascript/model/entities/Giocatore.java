package com.example.fantascript.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Giocatore extends BaseEntity
{
    private String nome;
    private String cognome;
    private Ruoli ruolo ;
    private int valutazione ;

    @ManyToOne
    @JoinColumn (name = "Id_squadra")
    private Squadra squadra ;



}


