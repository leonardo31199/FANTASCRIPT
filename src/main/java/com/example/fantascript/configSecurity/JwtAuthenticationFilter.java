package com.generation.ammazzon.configurations;

import com.example.fantascript.configSecurity.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**

 Filtro eseguito una sola volta per richiesta.
 <p>
 Estrae il token JWT dall’header Authorization, lo valida e,
 se valido, inserisce l’oggetto Authentication nello SecurityContext.*/
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";

	@Autowired
	private JwtService jwtService;             // servizio che crea/valida i token
	@Autowired
	private  UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain)
			throws ServletException, IOException {

		// 1) Prende l’header: Authorization: Bearer <token>
		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith(BEARER)) {
			chain.doFilter(request, response);        // nessun token → continua
			return;
		}

		final String jwt = authHeader.substring(BEARER.length());
		final String username = jwtService.extractUsername(jwt);

		// 2) Se l’utente non è già autenticato e il token è valido, crea Authentication
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
				&& jwtService.isTokenValid(jwt)) {

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authToken =
					new UsernamePasswordAuthenticationToken(
							userDetails,
							null,                       // nessuna credenziale da conservare
							userDetails.getAuthorities());

			authToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		// 3) passa al filtro successivo
		chain.doFilter(request, response);
	}
}