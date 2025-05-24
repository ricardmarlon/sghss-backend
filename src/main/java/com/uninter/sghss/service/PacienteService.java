package com.uninter.sghss.service;

import com.uninter.sghss.model.Paciente;
import com.uninter.sghss.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio relacionada aos Pacientes.
 * Interage com o PacienteRepository para persistência de dados.
 */
@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Salva um novo paciente no banco de dados.
     * Realiza validação para garantir que CPF e Email não sejam duplicados.
     * @param paciente O objeto Paciente a ser salvo.
     * @return O paciente salvo com o ID gerado.
     * @throws RuntimeException se o CPF ou Email já estiverem cadastrados.
     */
    public Paciente save(Paciente paciente) {
        if (pacienteRepository.findByCpf(paciente.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado.");
        }
        if (pacienteRepository.findByEmail(paciente.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }
        return pacienteRepository.save(paciente);
    }

    /**
     * Retorna uma lista de todos os pacientes cadastrados.
     * @return Uma lista de objetos Paciente.
     */
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    /**
     * Busca um paciente pelo seu ID.
     * @param id O ID do paciente.
     * @return Um Optional contendo o paciente, se encontrado.
     */
    public Optional<Paciente> findById(Long id) {
        return pacienteRepository.findById(id);
    }

    /**
     * Atualiza os dados de um paciente existente.
     * @param id O ID do paciente a ser atualizado.
     * @param pacienteDetails O objeto Paciente com os dados atualizados.
     * @return O paciente atualizado.
     * @throws RuntimeException se o paciente não for encontrado.
     */
    public Paciente update(Long id, Paciente pacienteDetails) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com o ID: " + id));

        // Atualiza os campos permitidos. Adicione validações adicionais se necessário.
        paciente.setNome(pacienteDetails.getNome());
        paciente.setEmail(pacienteDetails.getEmail());
        paciente.setTelefone(pacienteDetails.getTelefone());
        paciente.setEndereco(pacienteDetails.getEndereco());

        // O CPF não deve ser alterado após o cadastro para manter a integridade,
        // ou deve ter um processo de validação mais rigoroso.
        // paciente.setCpf(pacienteDetails.getCpf());

        return pacienteRepository.save(paciente);
    }

    /**
     * Deleta um paciente pelo seu ID.
     * @param id O ID do paciente a ser deletado.
     */
    public void deleteById(Long id) {
        pacienteRepository.deleteById(id);
    }
}