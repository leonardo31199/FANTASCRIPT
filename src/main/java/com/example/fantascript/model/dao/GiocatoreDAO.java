package com.example.fantascript.model.dao;

import com.example.fantascript.model.entities.Giocatore;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GiocatoreDAO extends JpaRepository<Giocatore, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE giocatore SET id_squadra = NULL", nativeQuery = true)
    void scollegaTuttiIGiocatori();

}
