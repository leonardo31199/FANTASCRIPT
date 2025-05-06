package com.example.fantascript.controller;

import com.example.fantascript.model.dao.UtenteDao;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;


@RestController
@RequestMapping("/api/auth")

public class UtenteController
{
    @Autowired
    private UtenteDao utenteDao;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationDto dto)
    {
        UtenteController daCreare = new UtenteController();
        daCreare.(dto.username());
        daCreare.setPassword(criptatore.encode(dto.password));
        daCreare.setMail(dto.mail());


        daCreare = utenteDao.save(daCreare);

        Carrello c = new Carrello();
        c.setUtente(daCreare);
        cDao.save(c);
    }



    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody LoginDto dto)
    {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(),dto.password()));

        String tokenizzato = jwtService.generateToken(auth);

        return new JwtResponseDto(tokenizzato);
    }

    public record LoginDto(
            String username,
            String password
    ){}

    public record RegistrationDto(
            String username,
            String mail,
            String password
    ){}

    public record JwtResponseDto(
            String token
    ){}

}
