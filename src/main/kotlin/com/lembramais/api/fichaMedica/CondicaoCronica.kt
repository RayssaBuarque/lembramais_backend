package com.lembramais.api.fichaMedica

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "condicoes_cronicas")
data class CondicaoCronica(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "paciente_id")
    val pacienteId: Long,

    val descricao: String
)

interface CondicaoCronicaRepository : JpaRepository<CondicaoCronica, Long> {
    fun findByPacienteId(pacienteId: Long): List<CondicaoCronica>
}