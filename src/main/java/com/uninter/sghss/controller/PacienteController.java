package com.uninter.sghss.controller;

import com.uninter.sghss.model.Paciente;
import com.uninter.sghss.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Para autorização
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    @PreAuthorize("isAuthenticated()") // Apenas usuários autenticados podem cadastrar
    public ResponseEntity<Paciente> createPaciente(@Valid @RequestBody Paciente paciente) {
        try {
            Paciente savedPaciente = pacienteService.save(paciente);
            return new ResponseEntity<>(savedPaciente, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Logar o erro para depuração
            System.err.println("Erro ao criar paciente: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Retorna 409 para CPF/Email duplicado
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Apenas usuários autenticados podem listar
    public ResponseEntity<List<Paciente>> getAllPacientes() {
        List<Paciente> pacientes = pacienteService.findAll();
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Apenas usuários autenticados podem buscar por ID
    public ResponseEntity<Paciente> getPacienteById(@PathVariable Long id) {
        return pacienteService.findById(id)
                .map(paciente -> new ResponseEntity<>(paciente, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Apenas usuários autenticados podem atualizar
    public ResponseEntity<Paciente> updatePaciente(@PathVariable Long id, @Valid @RequestBody Paciente pacienteDetails) {
        try {
            Paciente updatedPaciente = pacienteService.update(id, pacienteDetails);
            return new ResponseEntity<>(updatedPaciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 se não encontrado
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Apenas usuários autenticados podem deletar
    public ResponseEntity<HttpStatus> deletePaciente(@PathVariable Long id) {
        try {
            pacienteService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.err.println("Erro ao deletar paciente: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 se não encontrado
        }
    }
}