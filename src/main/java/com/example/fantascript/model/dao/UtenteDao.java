package com.example.fantascript.model.dao;

import com.example.fantascript.model.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteDao extends JpaRepository<Utente, Long>
{
    Utente findByUsernameAndPassword(String username, String password);

    Utente findByUsername(String username);

    //exist invece che dare l'utente con quello username, da solo vero o falso
    boolean existsByUsername(String username);

}
