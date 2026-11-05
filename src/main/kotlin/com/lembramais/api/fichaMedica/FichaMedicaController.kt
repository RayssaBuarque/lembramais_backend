package com.lembramais.api.fichaMedica

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// DTOs para receber os dados do JSON e evitar erro 400
data class AlergiaRequest(val nomeAlergia: String, val reacao: String?)
data class CondicaoRequest(val descricao: String)

@RestController
@RequestMapping("/api/v1")
class FichaMedicaController(
    private val alergiaRepo: AlergiaRepository,
    private val condicaoRepo: CondicaoCronicaRepository
) {
    // ==========================================
    // ALERGIAS
    // ==========================================
    @GetMapping("/pacientes/{pacienteId}/alergias")
    fun listarAlergias(@PathVariable pacienteId: Long) = alergiaRepo.findByPacienteId(pacienteId)

    @PostMapping("/pacientes/{pacienteId}/alergias")
    fun criarAlergia(@PathVariable pacienteId: Long, @RequestBody request: AlergiaRequest): Alergia {
        val novaAlergia = Alergia(
            pacienteId = pacienteId,
            nomeAlergia = request.nomeAlergia,
            reacao = request.reacao
        )
        return alergiaRepo.save(novaAlergia)
    }

    @PutMapping("/alergias/{alergiaId}")
    fun atualizarAlergia(@PathVariable alergiaId: Long, @RequestBody request: AlergiaRequest): ResponseEntity<Alergia> {
        val alergiaOpt = alergiaRepo.findById(alergiaId)
        if (alergiaOpt.isEmpty) return ResponseEntity.notFound().build()

        val alergia = alergiaOpt.get()
        val salva = alergiaRepo.save(alergia.copy(
            nomeAlergia = request.nomeAlergia,
            reacao = request.reacao
        ))
        return ResponseEntity.ok(salva)
    }

    @DeleteMapping("/alergias/{alergiaId}")
    fun deletarAlergia(@PathVariable alergiaId: Long): ResponseEntity<Void> {
        if (!alergiaRepo.existsById(alergiaId)) return ResponseEntity.notFound().build()
        alergiaRepo.deleteById(alergiaId)
        return ResponseEntity.noContent().build()
    }

    // ==========================================
    // CONDIÇÕES CRÔNICAS
    // ==========================================
    @GetMapping("/pacientes/{pacienteId}/condicoes-cronicas")
    fun listarCondicoes(@PathVariable pacienteId: Long) = condicaoRepo.findByPacienteId(pacienteId)

    @PostMapping("/pacientes/{pacienteId}/condicoes-cronicas")
    fun criarCondicao(@PathVariable pacienteId: Long, @RequestBody request: CondicaoRequest): CondicaoCronica {
        val novaCondicao = CondicaoCronica(
            pacienteId = pacienteId,
            descricao = request.descricao
        )
        return condicaoRepo.save(novaCondicao)
    }

    @PutMapping("/condicoes-cronicas/{condicaoId}")
    fun atualizarCondicao(@PathVariable condicaoId: Long, @RequestBody request: CondicaoRequest): ResponseEntity<CondicaoCronica> {
        val condicaoOpt = condicaoRepo.findById(condicaoId)
        if (condicaoOpt.isEmpty) return ResponseEntity.notFound().build()

        val condicao = condicaoOpt.get()
        val salva = condicaoRepo.save(condicao.copy(
            descricao = request.descricao
        ))
        return ResponseEntity.ok(salva)
    }

    @DeleteMapping("/condicoes-cronicas/{condicaoId}")
    fun deletarCondicao(@PathVariable condicaoId: Long): ResponseEntity<Void> {
        if (!condicaoRepo.existsById(condicaoId)) return ResponseEntity.notFound().build()
        condicaoRepo.deleteById(condicaoId)
        return ResponseEntity.noContent().build()
    }
}