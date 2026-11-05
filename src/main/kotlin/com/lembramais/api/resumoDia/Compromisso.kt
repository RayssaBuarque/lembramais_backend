package com.lembramais.api.resumoDia

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Entity
@Table(name = "compromissos")
data class Compromisso(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "paciente_id") val pacienteId: Long,
    val titulo: String,
    @Column(name = "data_hora") val dataHora: LocalDateTime,
    val local: String? = null,
    var status: String = "PENDENTE"
)

// DTO para receber a data como String do JSON e evitar erro 400
data class CompromissoRequest(
    val titulo: String,
    val dataHora: String, // Recebe "2026-11-05T10:00:00"
    val local: String? = null,
    val status: String = "PENDENTE"
)

interface CompromissoRepository : JpaRepository<Compromisso, Long> {
    fun findByPacienteIdOrderByDataHoraAsc(pacienteId: Long): List<Compromisso>
}

@RestController
@RequestMapping("/api/v1")
class CompromissoController(private val repo: CompromissoRepository) {
    @GetMapping("/pacientes/{pacienteId}/compromissos")
    fun listar(@PathVariable pacienteId: Long) = repo.findByPacienteIdOrderByDataHoraAsc(pacienteId)

    @PostMapping("/pacientes/{pacienteId}/compromissos")
    fun criar(@PathVariable pacienteId: Long, @RequestBody request: CompromissoRequest): Compromisso {
        // Converte a String recebida para LocalDateTime
        val dataConvertida = LocalDateTime.parse(request.dataHora)

        val novoCompromisso = Compromisso(
            pacienteId = pacienteId,
            titulo = request.titulo,
            dataHora = dataConvertida,
            local = request.local,
            status = request.status
        )

        return repo.save(novoCompromisso)
    }
}