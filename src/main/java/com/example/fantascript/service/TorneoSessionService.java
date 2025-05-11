package com.example.fantascript.service;

import com.example.fantascript.model.entities.Partita;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class TorneoSessionService {
    private final List<Partita> partite = new ArrayList<>();

    public void reset() {
        partite.clear();
    }

    public void aggiungi(List<Partita> nuovePartite) {
        partite.addAll(nuovePartite);
    }

    public List<Partita> getPartite() {
        return partite;
    }
}
