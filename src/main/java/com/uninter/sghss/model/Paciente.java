package com.uninter.sghss.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data; // Importa a anotação @Data do Lombok para gerar getters, setters, etc.
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Entidade Paciente que representa a tabela 'pacientes' no banco de dados.
 * Contém informações básicas do paciente.
 * Anotações Lombok (@Data) geram automaticamente getters, setters, toString, equals e hashCode.
 * Anotações JPA (@Entity, @Table, @Id, @GeneratedValue, @Column) mapeiam a classe para o banco de dados.
 * Anotações de validação (@NotBlank, @Email, @Pattern, @Size) garantem a integridade dos dados.
 */
@Entity
@Table(name = "pacientes")
@Data
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária auto-incrementável

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    @Column(nullable = false)
    private String nome; // Nome completo do paciente

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos.")
    @Column(nullable = false, unique = true) // CPF deve ser único
    private String cpf; // Cadastro de Pessoa Física

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Formato de email inválido.")
    @Column(nullable = false, unique = true) // Email deve ser único
    private String email; // Endereço de email do paciente

    @Column(nullable = true) // Pode ser nulo, mas é bom ter
    private String telefone; // Telefone de contato do paciente

    @Column(nullable = true)
    private String endereco; // Endereço completo do paciente

    // Construtor padrão (necessário para JPA)
    public Paciente() {}

    // Construtor com campos básicos para facilitar a criação
    public Paciente(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }
}