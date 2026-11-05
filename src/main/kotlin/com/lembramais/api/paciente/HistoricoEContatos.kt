package com.lembramais.api.paciente

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

// --- HISTÓRICO DE SAÚDE ---
@Entity
@Table(name = "historico_saude")
data class HistoricoSaude(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "paciente_id") val pacienteId: Long,
    val tipo: String, // Consulta ou Exame
    @Column(name = "data_evento") val dataEvento: LocalDateTime,
    val titulo: String,
    @Column(name = "profissional_nome") val profissionalNome: String? = null,
    val detalhes: String? = null
)

interface HistoricoRepository : JpaRepository<HistoricoSaude, Long> {
    fun findByPacienteIdOrderByDataEventoDesc(pacienteId: Long): List<HistoricoSaude>
}

// --- CONTATOS DE EMERGÊNCIA ---
@Entity
@Table(name = "contatos_emergencia")
data class ContatoEmergencia(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "paciente_id") val pacienteId: Long,
    val nome: String,
    val parentesco: String? = null,
    val telefone: String
)

interface ContatoRepository : JpaRepository<ContatoEmergencia, Long> {
    fun findByPacienteId(pacienteId: Long): List<ContatoEmergencia>
}

@RestController
@RequestMapping("/api/v1")
class HistoricoEContatosController(
    private val histRepo: HistoricoRepository,
    private val contatoRepo: ContatoRepository
) {
    @GetMapping("/pacientes/{pacienteId}/historico-saude")
    fun listarHistorico(@PathVariable pacienteId: Long) = histRepo.findByPacienteIdOrderByDataEventoDesc(pacienteId)

    @PostMapping("/pacientes/{pacienteId}/historico-saude")
    fun criarHistorico(@PathVariable pacienteId: Long, @RequestBody hist: HistoricoSaude) =
        histRepo.save(hist.copy(pacienteId = pacienteId))

    @GetMapping("/pacientes/{pacienteId}/contatos-emergencia")
    fun listarContatos(@PathVariable pacienteId: Long) = contatoRepo.findByPacienteId(pacienteId)
}