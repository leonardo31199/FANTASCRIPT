package com.example.fantascript.service;

import com.example.fantascript.model.dto.TelecronacaDTO;
import com.example.fantascript.model.dto.TelecronacaRequestDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class IaIa {

	/* ------------------------------------------------------------------ */
	/* ----------------------- CONFIGURAZIONE --------------------------- */
	/* ------------------------------------------------------------------ */

	/** Endpoint completo: es. http://127.0.0.1:1234/v1/chat/completions */
	private final String endpoint;

	/** Nome del modello, es. hermes‑3‑llama‑3.1‑8b */
	private final String model;

	/** Tentativi massimi se il JSON restituito non è valido */
	private static final int MAX_RETRY = 2;

	private final WebClient client;
	private final ObjectMapper mapper;

	public IaIa(@Value("${lmstudio.url}") String endpoint,
				@Value("${lmstudio.model}") String model,
				WebClient.Builder builder,
				ObjectMapper mapper) {

		this.endpoint = endpoint;
		this.model    = model;
		this.mapper   = mapper;

		this.client = builder
				.defaultHeader(HttpHeaders.USER_AGENT, "Fantascript/1.0")
				.build();
	}

	/* ------------------------------------------------------------------ */
	/* ------------------------- API PUBBLICA --------------------------- */
	/* ------------------------------------------------------------------ */

	public Mono<List<TelecronacaDTO>> telecronista(TelecronacaRequestDTO partita) {
		String userPrompt = buildUserPrompt(partita);

		ChatRequest req = buildChatRequest(
				List.of(
						new ChatRequest.Message("system",
								"Rispondi esclusivamente con un array JSON (nessun testo extra). " +
										"Ogni oggetto deve contenere le chiavi: \"minuto\" (int 1‑90), " +
										"\"commento\" (string), \"x\" (int 0‑93), \"y\" (int 0‑94)."+
								"Genera una lista di eventi per una partita di calcio simulata da 1 a 90 minuti. " +
										"Non solo goal: includi anche azioni come tiri, parate, dribbling, cross, contrasti, errori, ecc. " +
										"Ogni azione deve avere una posizione realistica in campo: " +
										"se il commento parla di 'cross dalla sinistra', le coordinate devono essere a sinistra; " +
										"se è un 'tiro centrale', la palla deve essere nella zona centrale davanti alla porta; " +
										"se è un recupero palla in difesa, deve essere vicino all'area difensiva. " +
										"Rendi la cronaca varia e avvincente, come farebbe un vero telecronista."

						),
						new ChatRequest.Message("user", userPrompt)
				)
		);

		return askModel(req, 0);
	}

	/* ------------------------------------------------------------------ */
	/* -------------------------- METODI PRIVATI ------------------------ */
	/* ------------------------------------------------------------------ */

	private String buildUserPrompt(TelecronacaRequestDTO p) {
		return String.format(
				"Sei un telecronista sportivo.\n" +
						"Fase: %s.\n" +
						"Partita %s vs %s (%d‑%d).\n" +
						"Gol %s: %s.\n" +
						"Gol %s: %s.\n" +
						"Le partite durano 90 minuti netti. Il minuto DEVE essere un intero ≤ 90.\n" +
						"Restituisci l’array JSON come da istruzioni di sistema e nient’altro.",
				p.getFase(),
				p.getSquadraCasa(), p.getSquadraTrasferta(),
				p.getGolCasa().length, p.getGolTrasferta().length,
				p.getSquadraCasa(), Arrays.toString(p.getGolCasa()),
				p.getSquadraTrasferta(), Arrays.toString(p.getGolTrasferta())
		);
	}

	private ChatRequest buildChatRequest(List<ChatRequest.Message> messages) {
		return new ChatRequest(
				model,
				messages,
				false,

				0.2,
				1.0
		);
	}

	private Mono<List<TelecronacaDTO>> askModel(ChatRequest req, int attempt) {
		return client.post()
				.uri(endpoint)                                   // endpoint completo
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(req)
				.retrieve()
				.bodyToMono(ChatResponse.class)
				.map(cr -> cr.choices().get(0).message().content())
				.flatMap(json -> {
					try {
						return Mono.just(mapper.readValue(
								json, new TypeReference<List<TelecronacaDTO>>() {}));
					} catch (JsonProcessingException e) {
						if (attempt >= MAX_RETRY) {
							return Mono.error(new RuntimeException(
									"JSON non valido dopo "
											+ (attempt + 1) + " tentativi: " + e.getMessage(), e));
						}
						log.warn("Tentativo {}: JSON non valido – {}", attempt + 1, e.getOriginalMessage());

						ChatRequest retryReq = buildChatRequest(
								List.of(
										new ChatRequest.Message("system",
												"Il JSON che hai restituito non è valido. Errore:\n"
														+ e.getOriginalMessage()
														+ "\n\nRiprova restituendo SOLO l’array JSON corretto."),
										new ChatRequest.Message("assistant", json)
								)
						);
						return askModel(retryReq, attempt + 1);
					}
				});
	}

	/* ------------------------------------------------------------------ */
	/* --------------------- RECORDS PER LA CHIAMATA -------------------- */
	/* ------------------------------------------------------------------ */

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record ChatRequest(
			String model,
			List<Message> messages,
			boolean stream,
			Double temperature,
			Double top_p
	) {
		public record Message(String role, String content) {}
		public record ResponseFormat(String type) {}
	}

	public record ChatResponse(
			String id,
			String object,
			long created,
			List<Choice> choices
	) {
		public record Choice(Message message) {
			public record Message(String role, String content) {}
		}
	}
}
//finito