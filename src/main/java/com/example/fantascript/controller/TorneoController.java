package com.example.fantascript.controller;


import com.example.fantascript.model.dto.PartitaDTO;
import com.example.fantascript.model.entities.Partita;
import com.example.fantascript.service.TorneoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/torneo")
@RequiredArgsConstructor
public class TorneoController {

    private final TorneoService torneoService;

    @PostMapping("/start")
    public List<PartitaDTO>avviaTorneo()
    {
        List<Partita> partite = torneoService.avviaTorneo();
//ciao
        return partite.stream()
                .map(p->new PartitaDTO(
                        p.getSquadraCasa().getNome(),
                        p.getGolCasa(),
                        p.getSquadraTrasferta().getNome(),
                        p.getGolTrasferta(),
                        p.getVincitore().getNome()
                ))
                .collect(Collectors.toList());
    }
}
