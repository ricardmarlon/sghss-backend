package com.uninter.sghss.controller;

import com.uninter.sghss.dto.AuthResponseDTO;
import com.uninter.sghss.dto.LoginRequestDTO;
import com.uninter.sghss.dto.RegisterRequestDTO;
import com.uninter.sghss.model.Usuario;
import com.uninter.sghss.service.AuthService;
import com.uninter.sghss.config.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para autenticação de usuários (registro e login).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired // Injetar AuthenticationManager diretamente no Controller
    private AuthenticationManager authenticationManager;

    /**
     * Endpoint: POST /api/auth/register
     * Objetivo: Registrar um novo usuário.
     * Parâmetros: JSON no corpo da requisição com username, email e password.
     * Respostas esperadas:
     * - 201 Created: Sucesso, retorna o usuário registrado.
     * - 400 Bad Request: Erro de validação.
     * - 409 Conflict: Nome de usuário ou email já existe.
     */
    @PostMapping("/register")
    public ResponseEntity<Usuario> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        try {
            Usuario newUser = new Usuario(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
            Usuario registeredUser = authService.register(newUser);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Retorna 409 para usuário/email duplicado
        }
    }

    /**
     * Endpoint: POST /api/auth/login
     * Objetivo: Realizar o login do usuário e gerar um token JWT.
     * Parâmetros: JSON no corpo da requisição com username e password.
     * Respostas esperadas:
     * - 200 OK: Sucesso, retorna o token JWT.
     * - 401 Unauthorized: Credenciais inválidas.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // A autenticação agora é feita diretamente no Controller
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication); // Define a autenticação no contexto
            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new AuthResponseDTO(jwt));
        } catch (Exception e) {
            System.err.println("Erro de autenticação: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); // Retorna 401 para credenciais inválidas
        }
    }
}