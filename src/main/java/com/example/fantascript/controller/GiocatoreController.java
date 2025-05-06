package com.example.fantascript.controller;

import com.example.fantascript.model.dao.GiocatoreDAO;
import com.example.fantascript.model.dao.SquadraDAO;
import com.example.fantascript.model.entities.Giocatore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fanta")
public class GiocatoreController {

    @Autowired
    private GiocatoreDAO gdao;

    @GetMapping("/lista")
    public List<Giocatore> lista()
    {
        return gdao.findAll();
    }


}
