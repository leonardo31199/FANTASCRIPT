package com.example.fantascript.controller;

import com.example.fantascript.service.IaIa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ControllerIa
{

    @Autowired
    IaIa ia;

    @GetMapping
    public telecronaca("/api/telecronaca")
{
    return"";
}
}
