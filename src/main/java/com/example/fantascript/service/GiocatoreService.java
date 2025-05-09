package com.example.fantascript.service;

import com.example.fantascript.model.dao.GiocatoreDAO;
import com.example.fantascript.model.entities.Giocatore;
import com.example.fantascript.model.dto.GiocatoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiocatoreService {

    private final GiocatoreDAO giocatoreDAO;

    /**
     * Restituisce tutti i giocatori in forma di entity.
     */
    public List<Giocatore> findAll() {
        return giocatoreDAO.findAll();
    }

    /**
     * Restituisce tutti i giocatori appartenenti alla squadra con l'id specificato.
     */
    public List<Giocatore> findBySquadraId(Long squadraId) {
        return giocatoreDAO.findAll().stream()
                .filter(g -> g.getSquadra() != null && g.getSquadra().getId().equals(squadraId))
                .collect(Collectors.toList());
    }

    /**
     * Converte una lista di entity Giocatore in DTO per il front-end.
     */
    public List<GiocatoreDTO> toDTOList(List<Giocatore> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Converte una singola entity Giocatore in GiocatoreDTO.
     */
    public GiocatoreDTO toDTO(Giocatore g) {
        return new GiocatoreDTO(
                g.getId(),
                g.getNome(),
                g.getCognome(),
                g.getRuolo().name(),
                g.getValutazione(),
                g.getNazionalita(),
                g.getNumeromaglietta()
        );
    }
}
