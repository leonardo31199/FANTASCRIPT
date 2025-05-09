package com.example.fantascript.controller;

import com.example.fantascript.model.dto.TelecronacaRequestDTO;
import com.example.fantascript.model.dto.TelecronacaDTO;
import com.example.fantascript.service.IaIa;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/telecronaca")
@RequiredArgsConstructor
public class ControllerIa {

    private final IaIa iaService;

    /**
     * Apre uno stream SSE: il client POSTa il TelecronacaRequestDTO e riceve
     * un TelecronacaDTO ogni 10 secondi fino ad esaurire la lista.
     */
    @PostMapping(value = "/stream", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamTelecronaca(@RequestBody TelecronacaRequestDTO req) {
        SseEmitter emitter = new SseEmitter(0L);  // no timeout

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                // 1) chiamo il servizio IA che mi restituisce la lista di commenti
                List<TelecronacaDTO> ticks = iaService.telecronista(req).block();

                // 2) mando un evento ogni 10s
                for (TelecronacaDTO tick : ticks) {
                    emitter.send(SseEmitter.event()
                            .name("commento")
                            .data(tick));
                    Thread.sleep(10_000);
                }
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }
}
