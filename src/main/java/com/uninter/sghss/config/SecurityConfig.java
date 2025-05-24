package com.uninter.sghss.config;

import com.uninter.sghss.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Esta é classe de configuração de segurança do Spring Security.
 * Podemos definir a cadeia de filtros de segurança, codificador de senha e gerenciador de autenticação.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita anotações @PreAuthorize
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Aqui foi configurado o provedor de autenticação DAO (Data Access Object).
     * Usa o AuthService para carregar usuários e o PasswordEncoder para verificar senhas.
     *
     * @param authService     O serviço de autenticação (AuthService) injetado pelo Spring.
     * @param passwordEncoder O codificador de senha (PasswordEncoder) injetado pelo Spring.
     * @return Uma instância de DaoAuthenticationProvider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(AuthService authService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Expõe o AuthenticationManager do Spring Security.
     *
     * @param authConfig A configuração de autenticação.
     * @return Uma instância de AuthenticationManager.
     * @throws Exception se houver um erro ao obter o AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configura a cadeia de filtros de segurança HTTP.
     * Define quais endpoints são públicos e quais exigem autenticação.
     * Configura o gerenciamento de sessão como STATELESS (para JWT).
     * Adiciona o filtro JWT antes do filtro de autenticação de nome de usuário/senha.
     *
     * @param http                    O objeto HttpSecurity para configurar a segurança.
     * @param jwtAuthenticationFilter O filtro JWT injetado pelo Spring.
     * @param authenticationProvider  O provedor de autenticação injetado pelo Spring.
     * @return Uma instância de SecurityFilterChain.
     * @throws Exception se houver um erro na configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, DaoAuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs RESTful
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Permite acesso público aos endpoints de autenticação
                        .anyRequest().authenticated() // Todos os outros endpoints exigem autenticação
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sessão stateless para JWT
                )
                .authenticationProvider(authenticationProvider) // Usa o provedor de autenticação injetado como parâmetro
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT injetado como parâmetro

        return http.build();
    }
}