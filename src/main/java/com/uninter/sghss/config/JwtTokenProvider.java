package com.uninter.sghss.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Este utilitário é para gerar e validar JSON Web Tokens (JWTs).
 * A chave secreta e o tempo de expiração são configuráveis no application.properties.
 *
 * Se encontrar algum erro durante a execução como "Cannot resolve method 'parserBuilder' in 'Jwts'",
 * isso geralmente indica uma versão desatualizada da dependência JJWT.
 * Certifique-se de que as versões de 'jjwt-api', 'jjwt-impl' e 'jjwt-jackson' no seu pom.xml
 * são 0.10.0 ou superiores (aqui usei 0.11.5).
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}") // Chave secreta para assinar o JWT (definida em application.properties)
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}") // Tempo de expiração do JWT em milissegundos
    private int jwtExpirationInMs;

    /**
     * Gera um token JWT para um usuário autenticado.
     * @param authentication O objeto Authentication contendo os detalhes do usuário.
     * @return O token JWT gerado.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes()); // Gera a chave a partir da string secreta

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Define o assunto do token (username)
                .setIssuedAt(new Date()) // Data de emissão
                .setExpiration(expiryDate) // Data de expiração
                .signWith(key, SignatureAlgorithm.HS512) // Assina o token com a chave e algoritmo HS512
                .compact();
    }

    /**
     * Obtém o nome de usuário (subject) de um token JWT.
     * @param token O token JWT.
     * @return O nome de usuário extraído do token.
     */
    public String getUsernameFromJWT(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Valida um token JWT.
     * @param authToken O token JWT a ser validado.
     * @return true se o token for válido, false caso contrário.
     */
    public boolean validateToken(String authToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            System.err.println("Assinatura JWT inválida.");
        } catch (MalformedJwtException ex) {
            System.err.println("Token JWT inválido.");
        } catch (ExpiredJwtException ex) {
            System.err.println("Token JWT expirado.");
        } catch (UnsupportedJwtException ex) {
            System.err.println("Token JWT não suportado.");
        } catch (IllegalArgumentException ex) {
            System.err.println("Cadeia de claims JWT vazia.");
        }
        return false;
    }
}