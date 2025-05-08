package com.example.fantascript;

import com.example.fantascript.model.dao.GiocatoreDAO;
import com.example.fantascript.model.entities.Giocatore;
import com.example.fantascript.model.entities.Ruoli;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FantascriptApplicationTests {

    @Autowired
    GiocatoreDAO gdao;
    @Test
    void contextLoads()
  {



      List<Giocatore> giocatori=new ArrayList<>();


      giocatori.add(new Giocatore("Luca", "Rossi", Ruoli.DIFENSORE, 62, "Italia", 3, null));
      giocatori.add(new Giocatore("Marco", "Bianchi", Ruoli.PORTIERE, 62, "Italia", 1, null));
      giocatori.add(new Giocatore("Carlos", "Martinez", Ruoli.ATTACCANTE, 82, "Spagna", 9, null));
      giocatori.add(new Giocatore("John", "Smith", Ruoli.ATTACCANTE, 76, "Inghilterra", 11, null));
      giocatori.add(new Giocatore("Yuto", "Nakamura", Ruoli.ATTACCANTE, 63, "Giappone", 13, null));
      giocatori.add(new Giocatore("Pierre", "Dubois", Ruoli.DIFENSORE, 67, "Francia", 5, null));
      giocatori.add(new Giocatore("Ivan", "Petrov", Ruoli.ATTACCANTE, 68, "Russia", 19, null));
      giocatori.add(new Giocatore("Ali", "Hassan", Ruoli.CENTROCAMPISTA, 78, "Egitto", 6, null));
      giocatori.add(new Giocatore("Sergio", "Ramos", Ruoli.DIFENSORE, 65, "Spagna", 4, null));
      giocatori.add(new Giocatore("David", "Johnson", Ruoli.CENTROCAMPISTA, 81, "USA", 8, null));
      giocatori.add(new Giocatore("Amir", "Farah", Ruoli.CENTROCAMPISTA, 69, "Iran", 23, null));
      giocatori.add(new Giocatore("Tariq", "Alami", Ruoli.DIFENSORE, 65, "Marocco", 2, null));
      giocatori.add(new Giocatore("Chen", "Li", Ruoli.DIFENSORE, 80, "Cina", 14, null));
      giocatori.add(new Giocatore("Takumi", "Ito", Ruoli.DIFENSORE, 79, "Giappone", 22, null));
      giocatori.add(new Giocatore("Leandro", "Silva", Ruoli.CENTROCAMPISTA, 86, "Brasile", 10, null));
      giocatori.add(new Giocatore("Moussa", "Dembele", Ruoli.PORTIERE, 67, "Francia", 12, null));
      giocatori.add(new Giocatore("Andrej", "Kramaric", Ruoli.CENTROCAMPISTA, 75, "Croazia", 17, null));
      giocatori.add(new Giocatore("Giorgio", "Ferrari", Ruoli.PORTIERE, 74, "Italia", 21, null));
      giocatori.add(new Giocatore("Salvatore", "Greco", Ruoli.ATTACCANTE, 67, "Italia", 18, null));
      giocatori.add(new Giocatore("Dominik", "Livakovic", Ruoli.DIFENSORE, 86, "Croazia", 16, null));
      giocatori.add(new Giocatore("Felipe", "Fernandez", Ruoli.CENTROCAMPISTA, 60, "Argentina", 28, null));
      giocatori.add(new Giocatore("Jamal", "Muller", Ruoli.CENTROCAMPISTA, 61, "Germania", 20, null));
      giocatori.add(new Giocatore("Niko", "Sanchez", Ruoli.ATTACCANTE, 79, "Messico", 7, null));
      giocatori.add(new Giocatore("Stefan", "Kim", Ruoli.PORTIERE, 72, "Corea del Sud", 24, null));
      giocatori.add(new Giocatore("Ahmed", "Tanaka", Ruoli.DIFENSORE, 85, "Emirati Arabi", 25, null));
      giocatori.add(new Giocatore("Omar", "Abdallah", Ruoli.PORTIERE, 67, "Qatar", 26, null));
      giocatori.add(new Giocatore("Luis", "Nguyen", Ruoli.PORTIERE, 60, "Vietnam", 27, null));
      giocatori.add(new Giocatore("Pedro", "Rahimi", Ruoli.DIFENSORE, 59, "Iran", 30, null));
      giocatori.add(new Giocatore("Giovanni", "Fontana", Ruoli.CENTROCAMPISTA, 69, "Italia", 29, null));
      giocatori.add(new Giocatore("Elia", "Vitale", Ruoli.DIFENSORE, 83, "Italia", 31, null));
      giocatori.add(new Giocatore("Matteo", "Pellegrini", Ruoli.PORTIERE, 63, "Italia", 33, null));
      giocatori.add(new Giocatore("Riccardo", "Moretti", Ruoli.PORTIERE, 79, "Italia", 34, null));
      giocatori.add(new Giocatore("Enzo", "Conti", Ruoli.ATTACCANTE, 67, "Italia", 36, null));
      giocatori.add(new Giocatore("Julian", "Gallo", Ruoli.CENTROCAMPISTA, 64, "Argentina", 37, null));
      giocatori.add(new Giocatore("Mateo", "Testa", Ruoli.DIFENSORE, 82, "Italia", 38, null));
      giocatori.add(new Giocatore("Ismael", "Barone", Ruoli.PORTIERE, 73, "Italia", 39, null));
      giocatori.add(new Giocatore("Noah", "Lombardi", Ruoli.CENTROCAMPISTA, 76, "Italia", 40, null));
      giocatori.add(new Giocatore("Kaan", "Mancini", Ruoli.ATTACCANTE, 81, "Italia", 41, null));
      giocatori.add(new Giocatore("Mehdi", "Costanzo", Ruoli.DIFENSORE, 72, "Tunisia", 42, null));
      giocatori.add(new Giocatore("Rayan", "Marino", Ruoli.ATTACCANTE, 76, "Italia", 43, null));
      giocatori.add(new Giocatore("Zlatan", "Serra", Ruoli.DIFENSORE, 59, "Bosnia", 44, null));
      giocatori.add(new Giocatore("Hakim", "Fiore", Ruoli.DIFENSORE, 62, "Marocco", 45, null));
      giocatori.add(new Giocatore("Elias", "Bruno", Ruoli.DIFENSORE, 61, "Italia", 46, null));
      giocatori.add(new Giocatore("Tobias", "De Luca", Ruoli.CENTROCAMPISTA, 81, "Italia", 47, null));
      giocatori.add(new Giocatore("Sacha", "Gentile", Ruoli.CENTROCAMPISTA, 63, "Italia", 48, null));
      giocatori.add(new Giocatore("Emil", "Longo", Ruoli.CENTROCAMPISTA, 76, "Italia", 49, null));
      giocatori.add(new Giocatore("Damian", "Leone", Ruoli.ATTACCANTE, 60, "Italia", 50, null));
      giocatori.add(new Giocatore("Thiago", "Parisi", Ruoli.CENTROCAMPISTA, 85, "Brasile", 32, null));
      giocatori.add(new Giocatore("Adem", "Russo", Ruoli.PORTIERE, 70, "Italia", 35, null));
      giocatori.add(new Giocatore("Ilyas", "Palmieri", Ruoli.CENTROCAMPISTA, 77, "Italia", 52, null));

      gdao.saveAll(giocatori);



  }

}
