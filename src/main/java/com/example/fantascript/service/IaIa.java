package com.example.fantascript.service;

import com.example.fantascript.model.dto.TelecronacaRequestDTO;
import com.example.fantascript.model.dto.TelecronacaDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IaIa {

	@Value("${lmstudio.url}")
	private String baseUrl;

	@Value("${lmstudio.model}")
	private String model;

	private final WebClient client = WebClient.builder().build();
	private final ObjectMapper mapper = new ObjectMapper();

	public Mono<List<TelecronacaDTO>> telecronista(TelecronacaRequestDTO partita) {
		String prompt = String.format(
				"Sei un telecronista sportivo. Fase: %s. Partita %s vs %s (%d-%d). " +
						"Marcatore: %s al %d'. Restituisci JSON array di oggetti {minuto, commento}:",
				partita.getFase(),
				partita.getSquadraCasa(), partita.getSquadraTrasferta(),
				partita.getGolCasa(), partita.getGolTrasferta(),
				partita.getMarcatori().get(0).getNome() + " " + partita.getMarcatori().get(0).getCognome(),                partita.getMarcatori().get(0).getMinuto()
		);

		ChatRequest req = new ChatRequest(
				model,
				List.of(
						new ChatRequest.Message("system", "Tu da oggi sei un telecronista esperto."),
						new ChatRequest.Message("user", prompt)
				),
				false
		);

		return client.post()
				.uri(baseUrl + "/v1/chat/completions")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(req)
				.retrieve()
				.bodyToMono(ChatResponse.class)
				// estrai solo il contenuto testuale
				.map(cr -> cr.choices().get(0).message().content())
				// parsifica il JSON in List<TelecronacaDTO> con gestione dell’eccezione
				.map(json -> {
					try {
						return mapper.readValue(json, new TypeReference<List<TelecronacaDTO>>() {});
					} catch (JsonProcessingException e) {
						throw new RuntimeException("Errore parsing JSON della telecronaca: " + e.getMessage(), e);
					}
				});
	}

	public record ChatRequest(
			String model,
			List<Message> messages,
			boolean stream
	) {
		public record Message(String role, String content) {}
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
