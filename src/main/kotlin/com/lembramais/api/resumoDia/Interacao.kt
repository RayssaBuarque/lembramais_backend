package com.lembramais.api.resumoDia

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.web.bind.annotation.*

@Entity
@Table(name = "interacoes_medicamentosas")
data class InteracaoMedicamentosa(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "medicamento_a_id") val medicamentoAId: Long,
    @Column(name = "medicamento_b_id") val medicamentoBId: Long,
    val gravidade: String,
    @Column(name = "descricao_orientacao") val descricaoOrientacao: String? = null
)

interface InteracaoRepository : JpaRepository<InteracaoMedicamentosa, Long> {
    // Query customizada para buscar interações onde os remédios pertencem ao paciente
    @Query("""
        SELECT i FROM InteracaoMedicamentosa i 
        JOIN Medicamento m1 ON i.medicamentoAId = m1.id 
        JOIN Medicamento m2 ON i.medicamentoBId = m2.id 
        WHERE m1.pacienteId = :pacienteId AND m2.pacienteId = :pacienteId
    """)
    fun findInteracoesByPaciente(pacienteId: Long): List<InteracaoMedicamentosa>
}

@RestController
@RequestMapping("/api/v1")
class AlertaController(private val interacaoRepo: InteracaoRepository) {
    @GetMapping("/pacientes/{pacienteId}/alertas")
    fun listarAlertas(@PathVariable pacienteId: Long): List<InteracaoMedicamentosa> {
        // Na vida real, aqui você juntaria interações com outros tipos de alertas (ex: remédio atrasado)
        return interacaoRepo.findInteracoesByPaciente(pacienteId)
    }
}