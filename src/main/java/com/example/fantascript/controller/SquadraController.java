package com.example.fantascript.controller;

import com.example.fantascript.model.dao.GiocatoreDAO;
import com.example.fantascript.model.dao.SquadraDAO;
import com.example.fantascript.model.dao.UtenteDao;
import com.example.fantascript.model.dto.GiocatoreDTO;
import com.example.fantascript.model.dto.SquadraDTO;
import com.example.fantascript.model.dto.SquadraResponseDTO;
import com.example.fantascript.model.entities.Giocatore;
import com.example.fantascript.model.entities.Ruoli;
import com.example.fantascript.model.entities.Squadra;
import com.example.fantascript.model.entities.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/team")



public class SquadraController {

    @PersistenceContext
    private EntityManager entityManager;


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

   // CHE MOSTRA SOLO LA SQUADRA DELL'UTENTE - PROVATO - FUNZIONANTE
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
            gDto.setNazionalita(g.getNazionalita());
            gDto.setNumeromaglietta(g.getNumeromaglietta());
            return gDto;
        }).collect(Collectors.toList());

        dto.setGiocatori(giocatori);

        return dto;
    }
//ciao
    @GetMapping("/squadre/tuttesquadre")
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


//ciao
@DeleteMapping("/squadre/reset")
public void resetSquadre() {
    gdao.scollegaTuttiIGiocatori();
    sdao.eliminaTutteLeSquadre();
}

    @GetMapping("/squadre/bot")
public List <Squadra>getBotSquadre()
{
    return sdao.findAll().stream()
            .filter(s-> s.getNome().toLowerCase().startsWith("botsquadra"))
            .collect(Collectors.toList());


}

    @PutMapping("/squadre/logo/{id}")
    public void aggiornaLogo(@PathVariable long id, @RequestParam String url)
    {
        Squadra s = sdao.findById(id).orElse(null);
        s.setLogo(url);
        sdao.save(s);
    }


    @PostMapping("/squadre/utente")
    public void creaSquadraUtente(@RequestBody SquadraDTO dto, Authentication auth) {
        Utente u = udao.findByUsername(auth.getName());

        Squadra s = new Squadra();
        s.setNome(dto.getNome());
        s.setLogo(dto.getLogo());
        s.setUtente(u);
        sdao.save(s);
        List<Giocatore> liberi = gdao.findAll()
                .stream()
                .filter(g -> g.getSquadra() == null)
                .collect(Collectors.toList());

        // Filtri per ruolo
        List<Giocatore> portieri = new ArrayList<>(liberi.stream().filter(g -> g.getRuolo() == Ruoli.PORTIERE).toList());
        List<Giocatore> difensori = new ArrayList<>(liberi.stream().filter(g -> g.getRuolo() == Ruoli.DIFENSORE).toList());
        List<Giocatore> centrocampisti = new ArrayList<>(liberi.stream().filter(g -> g.getRuolo() == Ruoli.CENTROCAMPISTA).toList());
        List<Giocatore> attaccanti = new ArrayList<>(liberi.stream().filter(g -> g.getRuolo() == Ruoli.ATTACCANTE).toList());

        // Verifica disponibilità
        if (portieri.size() < 4 || difensori.size() < 8 || centrocampisti.size() < 4 || attaccanti.size() < 5) {
            throw new RuntimeException("Non ci sono abbastanza giocatori per creare 4 squadre bilanciate");
        }

        // --------------------
        // SQUADRA DELL'UTENTE
        // --------------------
        Collections.shuffle(portieri);
        Collections.shuffle(difensori);
        Collections.shuffle(centrocampisti);
        Collections.shuffle(attaccanti);

        List<Giocatore> daAssegnare = new ArrayList<>();
        daAssegnare.add(portieri.remove(0));
        daAssegnare.add(difensori.remove(0));
        daAssegnare.add(difensori.remove(0));
        daAssegnare.add(centrocampisti.remove(0));
        daAssegnare.add(attaccanti.remove(0));

        for (Giocatore g : daAssegnare) {
            g.setSquadra(s);
        }

        s.setGiocatori(daAssegnare);

        gdao.saveAll(daAssegnare);

        // --------------------
        // BOT
        // --------------------
        List<String> nomiBot = List.of("Napoli", "Palermo", "Generation141");

        for (int i = 0; i < 3; i++) {
            if (portieri.size() < 1 || difensori.size() < 2 || centrocampisti.size() < 1 || attaccanti.size() < 1)
                break;

            Squadra bot = new Squadra();
            bot.setNome(nomiBot.get(i));
            bot.setLogo("bot" + (i + 1) + ".png");

            Collections.shuffle(portieri);
            Collections.shuffle(difensori);
            Collections.shuffle(centrocampisti);
            Collections.shuffle(attaccanti);

            List<Giocatore> gruppo = new ArrayList<>();
            gruppo.add(portieri.remove(0));
            gruppo.add(difensori.remove(0));
            gruppo.add(difensori.remove(0));
            gruppo.add(centrocampisti.remove(0));
            gruppo.add(attaccanti.remove(0));

            for (Giocatore g : gruppo) {
                g.setSquadra(bot);
            }

            bot.setGiocatori(gruppo);
            sdao.save(bot);
            gdao.saveAll(gruppo);
        }
    }



    @Autowired
    private SquadraDAO sdao;
    @Autowired
    private GiocatoreDAO gdao;
    @Autowired
    private UtenteDao udao;
    }
