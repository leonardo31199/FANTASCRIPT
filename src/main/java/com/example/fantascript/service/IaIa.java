package com.example.fantascript.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IaIa
{



    @Value("${lmstudio.url}")
    private String baseUrl;

    @Value("${lmstudio.model}")
    private String model;


    private final WebClient client = WebClient.builder().build();

    public Mono<MCQ> generateMcq(String topic) {
        String userPrompt = "";

        ChatRequest req = new ChatRequest(
                model,
                List.of(
                        new ChatRequest.Message("system", "tu da oggi sei un telecronista devi commentare tutto quello che succede in una partita di 2 minuti simulandola tu stesso"),
                        new ChatRequest.Message("user", userPrompt)
                ),
                false  // no streaming
        );

        WebClient.RequestHeadersSpec<?> spec = client.post()
                .uri(baseUrl + "/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req);

        return spec.retrieve()
                .bodyToMono(ChatResponse.class)
                .map(cr -> cr.choices().get(0).message().content())
                .map(this::parseMcqJson);
    }

    /** Converte la stringa JSON in MCQ */
    private MCQ parseMcqJson(String json) {
        try {
            return new ObjectMapper().readValue(json, MCQ.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON MCQ malformato: " + json, e);
        }
    }

    /**
     * Invia un prompt e restituisce la risposta completa (non stream).
     */
    public Mono<String> ask(String userPrompt)
    {
        ChatRequest req = new ChatRequest(
                model,
                List.of(
                        new ChatRequest.Message("system", "Sei un assistente utile."),
                        new ChatRequest.Message("user", userPrompt)
                ),
                false     // risposta in un unico payload
        );

        WebClient.RequestHeadersSpec<?> spec =
                    client.post()
                .uri(baseUrl + "/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req));


        return spec.retrieve()
                .bodyToMono(ChatResponse.class)
                .map(cr -> cr.choices().get(0).message().content());
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
            @JsonProperty("choices") List<Choice> choices
    ) {
        public record Choice(
                int index,
                Message message,
                @JsonProperty("finish_reason") String finishReason
        ) {
            public record Message(String role, String content) {}
        }
    }

    public record MCQ(String question, List<String> options, int answer_index) {}
}
