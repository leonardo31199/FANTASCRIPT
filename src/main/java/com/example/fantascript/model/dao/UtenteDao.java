package com.example.fantascript.model.dao;

import com.example.fantascript.model.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteDao extends JpaRepository<Utente, Long>
{
    Utente findByUsernameAndPassword(String username, String password);
}
