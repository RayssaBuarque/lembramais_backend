package com.lembramais.api.paciente

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

// --- HISTÓRICO DE SAÚDE ---
@Entity
@Table(name = "historico_saude")
data class HistoricoSaude(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "paciente_id") val pacienteId: Long,
    val tipo: String,
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

// DTO para evitar erro 400 no POST e PUT de Contatos
data class ContatoRequest(
    val nome: String,
    val parentesco: String?,
    val telefone: String
)

@RestController
@RequestMapping("/api/v1")
class HistoricoEContatosController(
    private val histRepo: HistoricoRepository,
    private val contatoRepo: ContatoRepository
) {
    // ==========================================
    // HISTÓRICO DE SAÚDE
    // ==========================================
    @GetMapping("/pacientes/{pacienteId}/historico-saude")
    fun listarHistorico(@PathVariable pacienteId: Long) = histRepo.findByPacienteIdOrderByDataEventoDesc(pacienteId)

    @PostMapping("/pacientes/{pacienteId}/historico-saude")
    fun criarHistorico(@PathVariable pacienteId: Long, @RequestBody hist: HistoricoSaude) =
        histRepo.save(hist.copy(pacienteId = pacienteId))

    // ==========================================
    // CONTATOS DE EMERGÊNCIA (CRUD Completo)
    // ==========================================
    @GetMapping("/pacientes/{pacienteId}/contatos-emergencia")
    fun listarContatos(@PathVariable pacienteId: Long) = contatoRepo.findByPacienteId(pacienteId)

    @PostMapping("/pacientes/{pacienteId}/contatos-emergencia")
    fun criarContato(@PathVariable pacienteId: Long, @RequestBody request: ContatoRequest): ContatoEmergencia {
        val novoContato = ContatoEmergencia(
            pacienteId = pacienteId,
            nome = request.nome,
            parentesco = request.parentesco,
            telefone = request.telefone
        )
        return contatoRepo.save(novoContato)
    }

    @PutMapping("/contatos-emergencia/{contatoId}")
    fun atualizarContato(@PathVariable contatoId: Long, @RequestBody request: ContatoRequest): ResponseEntity<ContatoEmergencia> {
        val contatoOpt = contatoRepo.findById(contatoId)
        if (contatoOpt.isEmpty) return ResponseEntity.notFound().build()

        val contato = contatoOpt.get()
        val salvo = contatoRepo.save(contato.copy(
            nome = request.nome,
            parentesco = request.parentesco,
            telefone = request.telefone
        ))
        return ResponseEntity.ok(salvo)
    }

    @DeleteMapping("/contatos-emergencia/{contatoId}")
    fun deletarContato(@PathVariable contatoId: Long): ResponseEntity<Void> {
        if (!contatoRepo.existsById(contatoId)) return ResponseEntity.notFound().build()
        contatoRepo.deleteById(contatoId)
        return ResponseEntity.noContent().build()
    }
}