package com.lembramais.api.paciente

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "pacientes")
data class Paciente(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "usuario_responsavel_id")
    val usuarioResponsavelId: Long,

    val nome: String,

    @Column(name = "data_nascimento")
    val dataNascimento: LocalDate? = null,

    @Column(name = "tipo_sanguineo")
    val tipoSanguineo: String? = null,

    @Column(name = "is_dependente")
    val isDependente: Boolean = false,

    @Column(name = "convenio_nome")
    val convenioNome: String? = null
)