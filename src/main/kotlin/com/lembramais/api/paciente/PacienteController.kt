package com.lembramais.api.paciente

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class PacienteController(private val pacienteRepository: PacienteRepository) {

    // Endpoint de teste rápido
    @GetMapping("/pacientes/ping")
    fun ping(): String = "API Lembra+ está rodando e conectada!"

    // Listar todos os pacientes (O retorno já incluirá as preferências automaticamente)
    @GetMapping("/pacientes")
    fun listarPacientes(): List<Paciente> {
        return pacienteRepository.findAll()
    }

    // 1. Endpoint para EDITAR as preferências
    @PutMapping("/pacientes/{id}/preferencias")
    fun atualizarPreferencias(
        @PathVariable id: Long,
        @RequestBody request: PreferenciaRequest
    ): ResponseEntity<Paciente> {
        val pacienteOpt = pacienteRepository.findById(id)
        if (pacienteOpt.isEmpty) return ResponseEntity.notFound().build()

        val paciente = pacienteOpt.get()
        // Atualiza apenas o campo de preferências
        val salvo = pacienteRepository.save(paciente.copy(preferencias = request.preferencias))

        return ResponseEntity.ok(salvo)
    }

    // 2. Endpoint para DELETAR as preferências (limpar o campo)
    @DeleteMapping("/pacientes/{id}/preferencias")
    fun deletarPreferencias(@PathVariable id: Long): ResponseEntity<Paciente> {
        val pacienteOpt = pacienteRepository.findById(id)
        if (pacienteOpt.isEmpty) return ResponseEntity.notFound().build()

        val paciente = pacienteOpt.get()
        // Seta o campo como nulo e salva
        val salvo = pacienteRepository.save(paciente.copy(preferencias = null))

        return ResponseEntity.ok(salvo)
    }

    // 3. Endpoint para deletar o paciente inteiro
    @DeleteMapping("/pacientes/{id}")
    fun deletarPaciente(@PathVariable id: Long): ResponseEntity<Void> {
        if (!pacienteRepository.existsById(id)) return ResponseEntity.notFound().build()
        pacienteRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}