package com.example.fantascript.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.sql.results.graph.Fetch;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Squadra extends BaseEntity {

    private String nome;
    private String logo;
    private Long valutazione_tot;

    @OneToMany (mappedBy = "squadra", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List <Giocatore> giocatori = new ArrayList<>();






}
