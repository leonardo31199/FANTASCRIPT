package com.example.fantascript.controller;

import com.example.fantascript.model.dao.GiocatoreDAO;
import com.example.fantascript.model.dao.SquadraDAO;
import com.example.fantascript.model.dao.UtenteDao;
import com.example.fantascript.model.dto.GiocatoreDTO;
import com.example.fantascript.model.dto.SquadraDTO;
import com.example.fantascript.model.dto.SquadraResponseDTO;
import com.example.fantascript.model.entities.Giocatore;
import com.example.fantascript.model.entities.Squadra;
import com.example.fantascript.model.entities.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/team")



public class SquadraController {
//recupera tutte le squadre (utente e bot)
    @GetMapping("/squadre")
    public List <Squadra> getAllSquadre()
    {
        return sdao.findAll();
    }

   //recupera la singola squadre per l'id
   @GetMapping("/squadre/{id}")
   public Squadra getSquadraById(@PathVariable Long id )
   {
       return sdao.findById(id)
               .orElseThrow(()->new RuntimeException("Squadra non trovata con id: " + id));

   }
    @GetMapping("/squadre/utente/squadrautente-dto")
    public SquadraResponseDTO getSquadraUtenteDTO(Authentication auth) {
        Utente u = udao.findByUsername(auth.getName());

        Squadra squadra = sdao.findAll().stream()
                .filter(s -> s.getUtente() != null && s.getUtente().equals(u))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Squadra dell'utente non trovata"));

        SquadraResponseDTO dto = new SquadraResponseDTO();
        dto.setId(squadra.getId());
        dto.setNome(squadra.getNome());
        dto.setLogo(squadra.getLogo());

        List<GiocatoreDTO> giocatori = squadra.getGiocatori().stream().map(g -> {
            GiocatoreDTO gDto = new GiocatoreDTO();
            gDto.setId(g.getId());
            gDto.setNome(g.getNome());
            gDto.setCognome(g.getCognome());
            gDto.setRuolo(g.getRuolo().name()); // enum -> stringa
            gDto.setValutazione(g.getValutazione());
            return gDto;
        }).collect(Collectors.toList());

        dto.setGiocatori(giocatori);

        return dto;
    }

    @GetMapping("/squadre/con-giocatori")
    public List<SquadraResponseDTO> getTutteLeSquadreConGiocatori() {
        List<Squadra> squadre = sdao.findAll();

        return squadre.stream().map(s -> {
            SquadraResponseDTO dto = new SquadraResponseDTO();
            dto.setId(s.getId());
            dto.setNome(s.getNome());
            dto.setLogo(s.getLogo());

            List<GiocatoreDTO> giocatori = s.getGiocatori().stream().map(g -> {
                GiocatoreDTO gDto = new GiocatoreDTO();
                gDto.setId(g.getId());
                gDto.setNome(g.getNome());
                gDto.setCognome(g.getCognome());
                gDto.setRuolo(g.getRuolo().name());
                gDto.setValutazione(g.getValutazione());
                return gDto;
            }).collect(Collectors.toList());

            dto.setGiocatori(giocatori);
            return dto;
        }).collect(Collectors.toList());
    }
    //recuperare la squadra utente
    @GetMapping("/squadre/utente/squadrautente")
    public Squadra getSquadraUtente()
    {
        return sdao.findAll().stream()
                .filter(s-> !s.getNome().toLowerCase().startsWith("botsquadra"))
                .findFirst()
                .orElseThrow(()->new RuntimeException("Nessuna squadra utente trovata"));
    }



    @DeleteMapping("/squadre/reset")
    public void resetSquadre()
    {

        List<Giocatore>tutti = gdao.findAll();
        for(Giocatore g:tutti)
        {
            g.setSquadra(null); //toglie il giocatore della squadra
        }
        gdao.saveAll(tutti);  //salva i giocatori liberi
        sdao.deleteAll(); // elimina le squadre

    }

@GetMapping("/squadre/bot")
public List <Squadra>getBotSquadre()
{
    return sdao.findAll().stream()
            .filter(s-> s.getNome().toLowerCase().startsWith("botsquadra"))
            .collect(Collectors.toList());


}






    @PostMapping("/squadre/utente")
    // Recupera tutti i giocatori dal database e filtra solo quelli che NON sono assegnati ad una squadra

    public void  creaSquadraUtente(@RequestBody SquadraDTO dto, Authentication auth) {


        Utente u=udao.findByUsername(auth.getName());

        Squadra s = new Squadra();
        s.setNome(dto.getNome());
        s.setLogo(dto.getLogo());
        s.setUtente(u);
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

        List<Giocatore> daAssegnare = liberi.stream().limit(5).collect(Collectors.toList());


        //Assegna giocatori selezionati alla squadra dell'utente
        for (Giocatore g : daAssegnare) {
            g.setSquadra(s); // Assegna ciascun giocatore alla squadra ricevuta nel body
        }
        // Assegna i giocatori alla squadra e salva sia la squadra che i giocatori nel database        s.setGiocatori(daAssegnare);
        sdao.save(s);
        gdao.saveAll(daAssegnare);

        // Rimuove i 5 giocatori appena assegnati dalla lista dei liberi
        liberi = liberi.subList(5, liberi.size());


        //Lista di nomi predefiniti per le squadre bot

        List<String>nomiBot=List.of("Napoli","Palermo","Generation141","Ubriaconi");

        int botCount = 4;

        for (int i = 0; i < botCount; i++) {
            if (liberi.size() < 5) break;
            Squadra bot = new Squadra();
            bot.setNome(nomiBot.get(i));
            bot.setLogo("bot" + (i + 1) + ".png");

            // Seleziona 5 giocatori per questa squadra bot
            List<Giocatore> gruppo = liberi.subList(0, 5);
            for (Giocatore g : gruppo) {
                g.setSquadra(bot);// Assegna ogni giocatore alla squadra bot

            }
            // Salva squadra bot e giocatori nel database
            bot.setGiocatori(gruppo);
            sdao.save(bot);
            gdao.saveAll(gruppo);

            // aggiorna la lista dei liberi escludendo quelli appena assegnati
            liberi = liberi.subList(5, liberi.size());
        }

    }


    @Autowired
    private SquadraDAO sdao;
    @Autowired
    private GiocatoreDAO gdao;
    @Autowired
    private UtenteDao udao;
    }
