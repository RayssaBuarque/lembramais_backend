package com.lembramais.api.fichaMedica

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class FichaMedicaController(
    private val alergiaRepo: AlergiaRepository,
    private val condicaoRepo: CondicaoCronicaRepository
) {
    @GetMapping("/pacientes/{pacienteId}/alergias")
    fun listarAlergias(@PathVariable pacienteId: Long) = alergiaRepo.findByPacienteId(pacienteId)

    @GetMapping("/pacientes/{pacienteId}/condicoes-cronicas")
    fun listarCondicoes(@PathVariable pacienteId: Long) = condicaoRepo.findByPacienteId(pacienteId)
}