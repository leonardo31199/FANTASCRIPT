package com.example.fantascript.controller;

import com.example.fantascript.model.dao.GiocatoreDAO;
import com.example.fantascript.model.dao.SquadraDAO;
import com.example.fantascript.model.entities.Giocatore;
import com.example.fantascript.model.entities.Ruoli;
import com.example.fantascript.model.entities.Squadra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/team")



public class SquadraController {

    @PostMapping("/squadre/utente")
    // Recupera tutti i giocatori dal database e filtra solo quelli che NON sono assegnati ad una squadra

    public Squadra creaSquadraUtente(@RequestBody Squadra s) {
        List<Giocatore> liberi = gdao.findAll()

                .stream()
                .filter(g -> g.getSquadra() == null)
                .collect(Collectors.toList());


        // Controllo: servono almeno 25 giocatori liberi per creare 5 squadre da 5 giocatori (1 utente + 4 bot)
        if (liberi.size() < 25) {
            throw new RuntimeException("Non ci sono giocatori liberi per creare una squadra");
        }

        // Mischia casualmente i giocatori liberi per evitare che siano sempre gli stessi
        Collections.shuffle(liberi);


        // Seleziona i primi 5 giocatori per assegnarli alla squadra dell’utente

        List<Giocatore> daAssegnare = new ArrayList<>();

        for (Ruoli ruolo : Ruoli.values()) {
            Giocatore trovato = liberi.stream()
                    .filter(g -> g.getRuolo() == ruolo)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Nessun giocatore libero per il ruolo: " + ruolo));

            daAssegnare.add(trovato);
            liberi.remove(trovato); // evita duplicazioni nei bot
        }




        //Assegna giocatori selezionati alla squadra dell'utente
        for (Giocatore g : daAssegnare) {
            g.setSquadra(s); // Assegna ciascun giocatore alla squadra ricevuta nel body
        }
        // Assegna i giocatori alla squadra e salva sia la squadra che i giocatori nel database        s.setGiocatori(daAssegnare);
        sdao.save(s);
        gdao.saveAll(daAssegnare);




        int botCount = 4;

        for (int i = 0; i < botCount; i++) {
            if (liberi.size() < 5) break;

            Squadra bot = new Squadra();
            bot.setNome("botsquadra" + (i + 1));
            bot.setLogo("bot" + (i + 1) + ".png");

            List<Giocatore> gruppo = new ArrayList<>();

            for (Ruoli ruolo : Ruoli.values()) {
                Giocatore trovato = liberi.stream()
                        .filter(g -> g.getRuolo() == ruolo)
                        .findFirst()
                        .orElse(null);

                if (trovato == null) {
                    System.out.println(" Non ci sono abbastanza giocatori per assegnare il ruolo: " + ruolo + " alla squadra bot " + bot.getNome());
                    break;
                }

                gruppo.add(trovato);
                liberi.remove(trovato);
            }

            if (gruppo.size() == 5) {
                for (Giocatore g : gruppo) {
                    g.setSquadra(bot);
                }
                bot.setGiocatori(gruppo);
                sdao.save(bot);
                gdao.saveAll(gruppo);
            } else {
                System.out.println(" Squadra bot " + bot.getNome() + " non creata per mancanza di ruoli disponibili.");
                break;
            }
        }
        return  s;
    }

    @Autowired
    private SquadraDAO sdao;
    @Autowired
    private GiocatoreDAO gdao;
    }
