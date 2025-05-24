package com.uninter.sghss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para respostas de autenticação.
 * Usado para enviar o token JWT de volta ao cliente após o login bem-sucedido.
 */
@Data
@AllArgsConstructor // Gera um construtor com todos os campos
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer"; // Tipo do token, padrão "Bearer"

    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}