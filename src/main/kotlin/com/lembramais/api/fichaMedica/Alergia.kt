package com.lembramais.api.fichaMedica

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "alergias")
data class Alergia(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "paciente_id")
    val pacienteId: Long,

    @Column(name = "nome_alergia")
    val nomeAlergia: String,

    val reacao: String? = null
)

interface AlergiaRepository : JpaRepository<Alergia, Long> {
    fun findByPacienteId(pacienteId: Long): List<Alergia>
}