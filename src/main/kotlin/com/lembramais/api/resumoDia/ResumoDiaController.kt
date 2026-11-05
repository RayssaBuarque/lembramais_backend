package com.lembramais.api.resumoDia

import com.lembramais.api.medicamento.HorarioMedicamento
import com.lembramais.api.medicamento.HorarioMedicamentoRepository
import com.lembramais.api.medicamento.MedicamentoRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

// DTO (Data Transfer Object) para organizar o JSON de retorno
data class ResumoDiaResponse(
    val dosesDoDia: List<HorarioMedicamento>,
    val proximoCompromisso: Compromisso?,
    val alertas: List<InteracaoMedicamentosa>
)

@RestController
@RequestMapping("/api/v1")
class ResumoDiaController(
    private val medicamentoRepo: MedicamentoRepository,
    private val horarioRepo: HorarioMedicamentoRepository,
    private val compromissoRepo: CompromissoRepository,
    private val interacaoRepo: InteracaoRepository
) {
    @GetMapping("/pacientes/{pacienteId}/resumo-dia")
    fun obterResumo(@PathVariable pacienteId: Long): ResumoDiaResponse {
        // 1. Pega todos os medicamentos do paciente
        val medicamentos = medicamentoRepo.findByPacienteId(pacienteId)

        // 2. Pega os horários desses medicamentos (aqui simplificamos pegando todos)
        val todasAsDoses = medicamentos.flatMap { med ->
            horarioRepo.findAll().filter { it.medicamentoId == med.id }
        }

        // 3. Pega o próximo compromisso (na data atual ou futura)
        val proximoComp = compromissoRepo.findByPacienteIdOrderByDataHoraAsc(pacienteId)
            .firstOrNull { it.dataHora.isAfter(LocalDateTime.now()) }

        // 4. Pega os alertas de interação
        val alertas = interacaoRepo.findInteracoesByPaciente(pacienteId)

        return ResumoDiaResponse(
            dosesDoDia = todasAsDoses,
            proximoCompromisso = proximoComp,
            alertas = alertas
        )
    }
}