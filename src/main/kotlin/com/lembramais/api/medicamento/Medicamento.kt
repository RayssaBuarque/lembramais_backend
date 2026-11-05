package com.lembramais.api.medicamento

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "medicamentos")
data class Medicamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "paciente_id")
    val pacienteId: Long,

    val nome: String,
    val dosagem: String? = null,

    @Column(name = "condicao_associada")
    val condicaoAssociada: String? = null,

    @Column(name = "medico_prescritor")
    val medicoPrescritor: String? = null,

    @OneToMany(mappedBy = "medicamentoId", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore // Evita loop infinito no JSON
    val horarios: List<HorarioMedicamento> = emptyList()
)

interface MedicamentoRepository : JpaRepository<Medicamento, Long> {
    fun findByPacienteId(pacienteId: Long): List<Medicamento>
}