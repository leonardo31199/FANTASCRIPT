package com.example.fantascript.service;

import com.example.fantascript.model.dao.SquadraDAO;
import com.example.fantascript.model.entities.Partita;
import com.example.fantascript.model.entities.Squadra;
import com.example.fantascript.model.dto.GiocatoreGoalDTO;
import com.example.fantascript.model.dto.PartitaDTO;
import com.example.fantascript.model.dto.TelecronacaRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TorneoService {

    private final SquadraDAO squadraDAO;
    private final GiocatoreService giocatoreService;
    private final IaIa iaService;

    private List<Partita> partite = new ArrayList<>(); // Lista delle partite

    /**
     * Simula un torneo a eliminazione diretta con 4 squadre: 2 semifinali + finale.
     * Restituisce la lista di 3 partite simulate (due semifinali e la finale).
     */
    public List<Partita> avviaTorneo() {
        // Recupera 4 squadre (utente + 3 bot)
        List<Squadra> squadre = new ArrayList<>(squadraDAO.findAll().stream()
                .filter(s -> s.getNome() != null)
                .limit(4)
                .toList());  // Lista modificabile per poter usare shuffle

        if (squadre.size() < 4) {
            throw new RuntimeException("Servono almeno 4 squadre per iniziare il torneo.");
        }

        // Mescola le squadre per randomizzare gli scontri
        Collections.shuffle(squadre);

        // Semifinali
        Partita semi1 = new Partita(squadre.get(0), squadre.get(1), null, 0, 0);
        Partita semi2 = new Partita(squadre.get(2), squadre.get(3), null, 0, 0);

        // Aggiungi le semifinali alla lista delle partite
        partite.add(semi1);
        partite.add(semi2);
        semi1.simula();

        semi2.simula();
        // Finale tra i due vincitori
        Partita finale = new Partita(semi1.getVincitore(), semi2.getVincitore(), null, 0, 0);
        partite.add(finale);

        finale.simula();
        //decommetare per la telecronaca ia
//        semi1.setTelecronaca(iaService.telecronista(buildTelecronacaRequest(semi1,"semifinale")).block());
//        semi2.setTelecronaca(iaService.telecronista(buildTelecronacaRequest(semi2,"semifinale")).block());
//        finale.setTelecronaca(iaService.telecronista(buildTelecronacaRequest(finale,"finale")).block());
        return partite;
    }

    /**
     * Prepara le semifinali senza avviare la simulazione.
     * Restituisce la lista delle semifinali senza risultati.
     */
    public List<Partita> preparaSemifinali() {
        // Recupera 4 squadre (utente + 3 bot)
        List<Squadra> squadre = new ArrayList<>(squadraDAO.findAll().stream()
                .filter(s -> s.getNome() != null)
                .limit(4)
                .toList());  // Lista modificabile per poter usare shuffle

        if (squadre.size() < 4) {
            throw new RuntimeException("Servono almeno 4 squadre per iniziare il torneo.");
        }

        // Mescola le squadre per randomizzare gli scontri
        Collections.shuffle(squadre);

        // Crea le semifinali senza simulare i risultati
        Partita semi1 = new Partita(squadre.get(0), squadre.get(1), null, 0, 0);
        Partita semi2 = new Partita(squadre.get(2), squadre.get(3), null, 0, 0);

        return List.of(semi1, semi2);  // Solo preparazione, non simulazione
    }

    /**
     * Restituisce solo le semifinali non ancora giocate.
     */
    public List<Partita> getSemifinaliNonGiocate() {
        // Filtra le partite per ottenere solo le semifinali non giocate
        return partite.stream()
                .filter(p -> p.getGolCasa() == 0 && p.getGolTrasferta() == 0)  // Partite con 0 gol
                .filter(p -> p.getVincitore() == null)  // Partite senza vincitore
                .limit(2)  // Solo le semifinali
                .collect(Collectors.toList());
    }

    /**
     * Restituisce la finale non ancora giocate.
     */
    public Partita getFinaleNonGiocata() {
        // Restituisce la finale se non è stata ancora giocata
        return partite.stream()
                .filter(p -> p.getGolCasa() == 0 && p.getGolTrasferta() == 0)  // Partite con 0 gol
                .filter(p -> p.getVincitore() == null)  // Finale senza vincitore
                .skip(2)  // La finale è l'ultima partita (dopo le semifinali)
                .findFirst()
                .orElse(null);
    }

    /**
     * Converte l’entity Partita in DTO basilare per restituirla al front.
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
     * Estrae un marcatore casuale dalla squadra vincente (con minuto casuale entro i 2').
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
     * Costruisce il DTO di contesto per la telecronaca IA:
     * include fase, risultato e marcatore selezionato.
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
}
