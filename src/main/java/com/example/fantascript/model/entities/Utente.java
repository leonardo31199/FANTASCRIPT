package com.example.fantascript.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Utente extends BaseEntity
{
    @Column(unique=true)
    private String Username;
    private String Password;
    @Column(unique=true)
    private String mail;
    private String roles;

    @OneToOne(mappedBy = "utente")
    private Squadra squadra;

    @OneToMany(mappedBy = "utente",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Squadra> squadrine = new ArrayList<>();

}
