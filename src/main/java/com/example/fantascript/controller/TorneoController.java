package com.example.fantascript.controller;

import com.example.fantascript.model.dto.PartitaDTO;
import com.example.fantascript.model.dto.TelecronacaDTO;
import com.example.fantascript.model.dto.TelecronacaRequestDTO;
import com.example.fantascript.model.entities.Partita;
import com.example.fantascript.service.TorneoService;
import com.example.fantascript.service.IaIa;
import com.example.fantascript.service.TorneoSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tornei")
@RequiredArgsConstructor
public class TorneoController {

    private final TorneoService torneoService;
    private final IaIa iaService;
    private final TorneoSessionService sessionService;
    private boolean semifinaleInCorso = false;
    private boolean finaleInCorso = false;

    private List<PartitaDTO> semifinali;
    private PartitaDTO finale;


    @GetMapping("/partite")
    public ResponseEntity<List<PartitaDTO>> getPartite() {
        // Recupera le partite simulate dal servizio TorneoService
        List<Partita> partite = torneoService.avviaTorneo();

        // Converte le partite in DTO per inviarle al frontend
        List<PartitaDTO> partitaDTOs = partite.stream()
                .map(torneoService::toDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(partitaDTOs, HttpStatus.OK);
    }
//    @GetMapping("/avvia")
//    public ResponseEntity<List<PartitaDTO>> avviaTorneo() {
//        try {
//            // Inizia il torneo, ma non simula le partite, solo prepara le semifinali
//            List<PartitaDTO> semifinali = torneoService.preparaSemifinali().stream()
//                    .map(torneoService::toDTO)
//                    .toList();
//
//            // Restituisce le semifinali non avviate
//            return new ResponseEntity<>(semifinali, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    // Avvia la telecronaca delle semifinali
//    @PostMapping("/telecronaca/semi")
//    public ResponseEntity<List<TelecronacaDTO>> telecronacaSemifinali(@RequestBody TelecronacaRequestDTO partita) {
//        try {
//            // Chiamo il servizio IA per la telecronaca delle semifinali
//            var telecronache = iaService.telecronista(partita).block();
//            return new ResponseEntity<>(telecronache, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // Dopo le semifinali, passiamo alla finale
//    @PostMapping("/continua")
//    public ResponseEntity<PartitaDTO> continuaConFinale() {
//        if (!semifinaleInCorso) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        // Calcolare i vincitori delle semifinali
//        String semi1Vincitore = semifinali.get(0).getGolCasa() > semifinali.get(0).getGoltrasferta()
//                ? semifinali.get(0).getSquadraCasa()
//                : semifinali.get(0).getSquadraTrasferta();
//
//        String semi2Vincitore = semifinali.get(1).getGolCasa() > semifinali.get(1).getGoltrasferta()
//                ? semifinali.get(1).getSquadraCasa()
//                : semifinali.get(1).getSquadraTrasferta();
//
//        // Creare la finale con i vincitori delle semifinali
//        finale = new PartitaDTO(
//                semi1Vincitore, // Nome della squadra casa della finale
//                0, // Gol casa
//                semi2Vincitore, // Nome della squadra trasferta della finale
//                0, // Gol trasferta
//                "" // Vincitore della finale (sarà vuoto inizialmente, lo decideremo più tardi)
//        );
//
//        semifinaleInCorso = false;  // Fase semifinali terminata
//        finaleInCorso = true;  // Inizia la fase finale
//        return new ResponseEntity<>(finale, HttpStatus.OK);
//    }

    // Avvia la telecronaca della finale
//    @PostMapping("/telecronaca/finale")
//    public ResponseEntity<List<TelecronacaDTO>> telecronacaFinale(@RequestBody TelecronacaRequestDTO partita) {
//        try {
//            var telecronache = iaService.telecronista(partita).block();
//            return new ResponseEntity<>(telecronache, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
