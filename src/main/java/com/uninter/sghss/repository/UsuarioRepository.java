package com.uninter.sghss.repository;

import com.uninter.sghss.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Usuario.
 * Estende JpaRepository para fornecer operações CRUD básicas.
 * Inclui métodos de busca personalizados para username e email.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username); // Busca um usuário pelo nome de usuário
    Optional<Usuario> findByEmail(String email); // Busca um usuário pelo email
}