package com.lembramais.api.medicamento

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalTime

@Entity
@Table(name = "horarios_medicamentos")
data class HorarioMedicamento(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "medicamento_id")
    val medicamentoId: Long,

    val horario: LocalTime,

    val frequencia: String,

    @Column(name = "status_dose")
    var statusDose: String = "PENDENTE"
)

interface HorarioMedicamentoRepository : JpaRepository<HorarioMedicamento, Long>