package com.example.fantascript.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class Partita {
    private String id;
  private Squadra squadraCasa;
  private Squadra squadraTrasferta;
  private Squadra vincitore;
  private int golCasa;
  private int golTrasferta;

    public Partita(Squadra squadraCasa, Squadra squadraTrasferta, Squadra vincitore, int golCasa, int golTrasferta) {
        this.squadraCasa = squadraCasa;
        this.squadraTrasferta = squadraTrasferta;
        this.vincitore = vincitore;
        this.golCasa = golCasa;
        this.golTrasferta = golTrasferta;
    }

    //simula partita tra casa e trasferta

    public void simula ()
    {
        id=squadraCasa.getNome()+"-"+squadraTrasferta.getNome();


        // gol in casa da 0 a 5
        this.golCasa = (int) (Math.random()*6);

        //gol in trasfera **
        this.golTrasferta = (int) (Math.random()*6);

        if (golCasa>golTrasferta)
        {
            vincitore= squadraCasa; //vince squadra in casa

        } else if (golCasa<golTrasferta) {
            vincitore = squadraTrasferta; //vince trasferta
        }
          else
          {
              //pareggio: si simulano i rigori in caso di pareggio
              int rigoriCasa = 0;
              int rigoriTrasferta = 0;

              //ogni squadra ha a disposizione 5 rigori

              for (int i = 0; i < 5; i ++)
              {
                  if (Math.random()>0.4)rigoriCasa++; //60 per cento di poss do segnare
                  if (Math.random()>0.4)rigoriTrasferta++; // idem

              }

              //se ancora pari rigori a oltranza
              while (rigoriCasa==rigoriTrasferta)
              {
                  if (Math.random() > 0.4) rigoriCasa++;
                  if (Math.random() > 0.4) rigoriTrasferta++;
              } //determina il vicnitore dei rigori

              vincitore= rigoriCasa>rigoriTrasferta?squadraCasa:squadraTrasferta;

              System.out.println("Fine partita: Pari--> Si va ai Rigori!");
              System.out.println("Casa " + squadraCasa.getNome()+": " + rigoriCasa + " rigori");
              System.out.println("Trasferta " + squadraTrasferta.getNome()+": " + rigoriTrasferta + " rigori");


        }
        }


    }
