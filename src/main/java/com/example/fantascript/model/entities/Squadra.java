package com.example.fantascript.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.results.graph.Fetch;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data

public class Squadra extends BaseEntity {

    private String nome;
    private String logo;
    private Long valutazione_tot;

    @OneToMany (mappedBy = "squadra", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE,orphanRemoval = false)
    private List <Giocatore> giocatori = new ArrayList<>();

    @OneToOne(optional = true)
    @JoinColumn(name="id_utente")
    private Utente utente;



//ciao

}
