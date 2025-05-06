package com.example.fantascript.service;

import com.example.fantascript.model.dao.SquadraDAO;
import com.example.fantascript.model.entities.Partita;
import com.example.fantascript.model.entities.Squadra;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TorneoService {
 //ciao
    private final SquadraDAO squadraDAO;

    public List<Partita> avviaTorneo() {
        // Recupera 4 squadre per fare il torneo (utente + 3 bot)
        List<Squadra> squadre = squadraDAO.findAll().stream()
                .filter(s -> s.getNome() != null)
                .limit(4)
                .toList();

        if (squadre.size() < 4) {
            throw new RuntimeException("Servono almeno 4 squadre per iniziare il torneo.");
        }

        // Mischia l’ordine per randomizzare gli scontri
        Collections.shuffle(squadre);

        // Semifinali
        Partita semifinale1 = new Partita(squadre.get(0), squadre.get(1), null, 0, 0);
        semifinale1.simula();

        Partita semifinale2 = new Partita(squadre.get(2), squadre.get(3), null, 0, 0);
        semifinale2.simula();

        // Finale tra i due vincitori
        Partita finale = new Partita(
                semifinale1.getVincitore(),
                semifinale2.getVincitore(),
                null, 0, 0
        );
        finale.simula();

        // Ritorna tutte le partite giocate
        return List.of(semifinale1, semifinale2, finale);
    }
}
