package com.duoc.cursos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${azure.tenant-id}")
    private String tenantId;

    @Value("${azure.client-id}")
    private String clientId;

    @Value("${azure.client-secret}")
    private String clientSecret;

    @Value("${azure.scope}")
    private String scope;

    @PostMapping("/token")
    public ResponseEntity<?> obtenerToken() {
        String tokenUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";
        try {
            WebClient webClient = WebClient.builder().build();
            Map<String, Object> respuesta = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue("grant_type=client_credentials"
                            + "&client_id=" + clientId
                            + "&client_secret=" + clientSecret
                            + "&scope=" + scope)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "No se pudo obtener el token: " + e.getMessage()));
        }
    }
}