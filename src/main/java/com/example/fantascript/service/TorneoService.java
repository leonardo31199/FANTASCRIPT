package com.example.fantascript.service;

import com.example.fantascript.model.dao.SquadraDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TorneoService {

    private final SquadraDAO squadraDAO;

    /**
     * Simula un torneo tra 4 squadre:
     *  - due semifinali
     *  - una finale
     *  - ogni partita può finire ai rigori
     * @return lista delle partite giocate
     */
}
