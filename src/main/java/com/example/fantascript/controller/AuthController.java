package com.example.fantascript.controller;

import com.example.fantascript.configSecurity.JwtService;
import com.example.fantascript.model.dao.UtenteDao;

import com.example.fantascript.model.entities.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
	@Autowired
	private JwtService jwtService;
	@Autowired
	private PasswordEncoder criptatore;
	@Autowired
	private UtenteDao udao;
	@Autowired
	private AuthenticationManager authManager;


	@PostMapping("/register")
	public void register(@RequestBody RegistrationDto dto)
	{
		Utente daCreare = new Utente();
		daCreare.setUsername(dto.username());
		daCreare.setPassword(criptatore.encode(dto.password));
		daCreare.setMail(dto.mail());
		daCreare.setRoles("ROLE_USER");
		daCreare = udao.save(daCreare);
	}

	@PostMapping("/login")
	public JwtResponseDto login(@RequestBody LoginDto dto)
	{
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(),dto.password()));

		String tokenizzato = jwtService.generateToken(auth);

		return new JwtResponseDto(tokenizzato);

	}

	@GetMapping("/test")
	public String dammiNomeUtente(Authentication auth)//lui lo riempie in automatico con l'utente a cui appartiene il JWT
	{
		String nomeUtente = auth.getName();

		return nomeUtente;
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
