package com.example.fantascript.controller;

import com.example.fantascript.model.dao.GiocatoreDAO;
import com.example.fantascript.model.dao.SquadraDAO;
import com.example.fantascript.model.entities.Giocatore;
import com.example.fantascript.model.entities.Squadra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/team")



public class SquadraController {

    @PostMapping ("/squadre/utente")
    public Squadra creaSquadraUtente (@RequestBody Squadra s) {
        List<Giocatore> liberi = gdao.findAll()
                //recupera tutti i giocatori non asssegnati
                .stream()
                .filter(g -> g.getSquadra() == null)
                .collect(Collectors.toList());


        //verifica dei giocatorio disponibili (5 giocatori rimanenti)
        if (liberi.size() > 5) {
            throw new RuntimeException("Non ci sono giocatori liberi per creare una squadra");
        }

        //mescola la lista dei giocatri liberi (non scelti dall'utente) e seleziona i primi 5.
        Collections.shuffle(liberi);

        List<Giocatore> daAssegnare = liberi.stream().limit(5).collect(Collectors.toList());


//Assegna giocatori selezionati alla squadra dell'utente
        for (Giocatore g : daAssegnare) {
            g.setSquadra(s);
        }
        //imposta i giocatori nella squadra dell'utente e salva
        s.setGiocatori(daAssegnare);
        sdao.save(s);
        gdao.saveAll(daAssegnare);
        //
        int botCount = 4;

        for (int i = 0; i < botCount; i++) {
            if (liberi.size() < 5) break;
            Squadra bot = new Squadra();
            bot.setNome("botsquadra" + (i + 1));
            bot.setLogo("bot" + (i + 1) + ".png");
        }


        List<Giocatore> gruppo = liberi.subList(0, 5);
        for (Giocatore g; gruppo) ;
        {
            g.setSquadra(bot)

        }

    }

















        @Autowired
        private SquadraDAO sdao;
    @Autowired
    private GiocatoreDAO gdao;
    }
