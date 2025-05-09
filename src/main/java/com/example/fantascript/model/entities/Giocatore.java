package com.example.fantascript.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Giocatore extends BaseEntity
{
    private String nome;
    private String cognome;
    private Ruoli ruolo ;
    private int valutazione ;
    private String nazionalita;
    private int numeromaglietta;



    @ManyToOne
    @JoinColumn (name = "Id_squadra")
    @JsonIgnore
    private Squadra squadra ;



}


