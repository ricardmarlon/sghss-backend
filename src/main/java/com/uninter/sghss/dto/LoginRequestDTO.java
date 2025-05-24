package com.uninter.sghss.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para requisições de login.
 * Usado para receber os dados de username e password do cliente.
 */
@Data
public class LoginRequestDTO {
    @NotBlank(message = "O nome de usuário é obrigatório.")
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    private String password;
}