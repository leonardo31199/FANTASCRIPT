package com.example.fantascript.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Utente extends BaseEntity
{
    private String Username;
    private String Password;
    private String mail;

    @OneToOne(mappedBy = "Squadra")
    private Squadra squadra;

}
