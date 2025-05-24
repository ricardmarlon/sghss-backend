package com.uninter.sghss.repository;

import com.uninter.sghss.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Paciente.
 * Estende JpaRepository para fornecer operações CRUD básicas.
 * Inclui métodos de busca personalizados para CPF e Email.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCpf(String cpf); // Busca um paciente pelo CPF
    Optional<Paciente> findByEmail(String email); // Busca um paciente pelo Email
}