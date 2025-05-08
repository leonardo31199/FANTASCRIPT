package com.example.fantascript.model.dao;

import com.example.fantascript.model.entities.Squadra;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SquadraDAO extends JpaRepository <Squadra , Long > {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM squadra", nativeQuery = true)
    void eliminaTutteLeSquadre();
}
