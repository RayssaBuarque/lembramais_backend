package com.lembramais.api.paciente

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/pacientes")
class PacienteController(private val pacienteRepository: PacienteRepository) {

    // Endpoint de teste rápido
    @GetMapping("/ping")
    fun ping(): String = "API Lembra+ está rodando e conectada!"

    // Endpoint Real: Listar todos os pacientes do banco
    @GetMapping
    fun listarPacientes(): List<Paciente> {
        return pacienteRepository.findAll()
    }

    @DeleteMapping("/pacientes/{id}")
    fun deletarPaciente(@PathVariable id: Long) {
        pacienteRepository.deleteById(id)
    }
}