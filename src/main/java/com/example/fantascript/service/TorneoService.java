package com.example.fantascript.service;

import com.example.fantascript.model.dao.SquadraDAO;
import com.example.fantascript.model.entities.Partita;
import com.example.fantascript.model.entities.Squadra;
import com.example.fantascript.model.dto.GiocatoreGoalDTO;
import com.example.fantascript.model.dto.PartitaDTO;
import com.example.fantascript.model.dto.TelecronacaRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@SessionScope
@RequiredArgsConstructor
public class TorneoService {

    private final SquadraDAO squadraDAO;
    private final GiocatoreService giocatoreService;
private final IaIa iaService;

    // Lista delle partite che vive per tutta la durata della sessione
    private final List<Partita> partite = new ArrayList<>();

    /**
     * Simula un torneo a eliminazione diretta con 4 squadre: 2 semifinali + finale.
     * Ogni volta che viene invocato, svuota la lista interna e rigenera esattamente 3 partite.
     */
    public List<Partita> avviaTorneo() {
        // 1) Svuota la lista interna
        partite.clear();

        // 2) Recupera 4 squadre e randomizza
        List<Squadra> squadre = squadraDAO.findAll().stream()
                .filter(s -> s.getNome() != null)
                .limit(4)
                .collect(Collectors.toCollection(ArrayList::new));

        if (squadre.size() < 4) {
            throw new RuntimeException("Servono almeno 4 squadre per iniziare il torneo.");

        }
        Collections.shuffle(squadre);

        // 3) Simula le due semifinali
        Partita semi1 = new Partita(squadre.get(0), squadre.get(1), null, 0, 0);
        Partita semi2 = new Partita(squadre.get(2), squadre.get(3), null, 0, 0);
        semi1.simula();
        semi2.simula();

        // 4) Simula la finale tra i vincitori
        Squadra v1 = semi1.getVincitore();
        Squadra v2 = semi2.getVincitore();
        Partita finale = new Partita(v1, v2, null, 0, 0);
        finale.simula();

        // 5) Popola la lista in sessione
        partite.add(semi1);
        partite.add(semi2);
        partite.add(finale);
//decommetare per la telecronaca ia
        semi1.setTelecronaca(iaService.telecronista(buildTelecronacaRequest(semi1,"semifinale")).block());
        semi2.setTelecronaca(iaService.telecronista(buildTelecronacaRequest(semi2,"semifinale")).block());
        finale.setTelecronaca(iaService.telecronista(buildTelecronacaRequest(finale,"finale")).block());
        // 6) Restituisci una lista non modificabile
        return List.copyOf(partite);
    }

    /**
     * Prepara le semifinali senza avviare la simulazione.
     */
    public List<Partita> preparaSemifinali() {
        List<Squadra> squadre = squadraDAO.findAll().stream()
                .filter(s -> s.getNome() != null)
                .limit(4)
                .collect(Collectors.toCollection(ArrayList::new));

        if (squadre.size() < 4) {
            throw new RuntimeException("Servono almeno 4 squadre per iniziare il torneo.");
        }
        Collections.shuffle(squadre);

        Partita semi1 = new Partita(squadre.get(0), squadre.get(1), null, 0, 0);
        Partita semi2 = new Partita(squadre.get(2), squadre.get(3), null, 0, 0);
        return List.of(semi1, semi2);
    }

    /**
     * Restituisce le semifinali non ancora giocate.
     */
    public List<Partita> getSemifinaliNonGiocate() {
        return partite.stream()
                .filter(p -> p.getGolCasa() == 0 && p.getGolTrasferta() == 0)
                .filter(p -> p.getVincitore() == null)
                .limit(2)
                .collect(Collectors.toList());
    }

    /**
     * Restituisce la finale non ancora giocata.
     */
    public Partita getFinaleNonGiocata() {
        return partite.stream()
                .filter(p -> p.getGolCasa() == 0 && p.getGolTrasferta() == 0)
                .filter(p -> p.getVincitore() == null)
                .skip(2)
                .findFirst()
                .orElse(null);
    }

    /**
     * Converte un’entity Partita in DTO per il front.
     */
    public PartitaDTO toDTO(Partita p) {
        return new PartitaDTO(
                p.getSquadraCasa().getNome(),
                p.getGolCasa(),
                p.getSquadraTrasferta().getNome(),
                p.getGolTrasferta(),
                p.getVincitore() != null ? p.getVincitore().getNome() : "In corso",
                p.getTelecronaca()
        );
    }

    /**
     * Seleziona a caso un marcatore dalla squadra vincente.
     */
    public GiocatoreGoalDTO pickRandomScorer(Partita p) {
        Long squadraId = p.getVincitore().getId();
        var roster = giocatoreService.findBySquadraId(squadraId);
        if (roster.isEmpty()) {
            throw new RuntimeException("Nessun giocatore per squadra " + p.getVincitore().getNome());
        }
        var scelto = roster.get(new Random().nextInt(roster.size()));
        int minuto = 1 + new Random().nextInt(120);
        return new GiocatoreGoalDTO(
                scelto.getId(),
                scelto.getNome(),
                scelto.getCognome(),
                minuto
        );
    }

    /**
     * Costruisce il DTO per la telecronaca IA.
     */
    public TelecronacaRequestDTO buildTelecronacaRequest(Partita partita, String fase) {
        PartitaDTO dto = toDTO(partita);
        GiocatoreGoalDTO marcatore = pickRandomScorer(partita);
        return new TelecronacaRequestDTO(
                fase,
                dto.getSquadraCasa(),
                dto.getGolCasa(),
                dto.getSquadraTrasferta(),
                dto.getGoltrasferta(),
                List.of(marcatore)
        );
    }

    /**
     * Reset manuale delle partite (opzionale).
     */
    public void resetTorneo() {
        partite.clear();
    }
}
