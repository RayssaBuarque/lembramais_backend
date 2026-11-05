package com.lembramais.api.medicamento

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.LocalTime

// DTO para receber os dados de criação
data class MedicamentoRequest(
    val nome: String,
    val dosagem: String,
    val condicaoAssociada: String?,
    val medicoPrescritor: String?,
    val horarios: List<LocalTime> // Ex: ["08:00", "20:00"]
)

@RestController
@RequestMapping("/api/v1")
class MedicamentoController(
    private val medicamentoRepo: MedicamentoRepository,
    private val horarioRepo: HorarioMedicamentoRepository
) {
    @GetMapping("/pacientes/{pacienteId}/medicamentos")
    fun listarPorPaciente(@PathVariable pacienteId: Long) = medicamentoRepo.findByPacienteId(pacienteId)

    @PostMapping("/pacientes/{pacienteId}/medicamentos")
    fun cadastrar(@PathVariable pacienteId: Long, @RequestBody request: MedicamentoRequest): Medicamento {
        val novoMedicamento = medicamentoRepo.save(
            Medicamento(
                pacienteId = pacienteId,
                nome = request.nome,
                dosagem = request.dosagem,
                condicaoAssociada = request.condicaoAssociada,
                medicoPrescritor = request.medicoPrescritor
            )
        )

        // Salva os horários vinculados
        request.horarios.forEach { hora ->
            horarioRepo.save(
                HorarioMedicamento(medicamentoId = novoMedicamento.id!!, horario = hora, frequencia = "Diário")
            )
        }
        return novoMedicamento
    }

    // Endpoint para atualizar o status da dose (PATCH)
    @PatchMapping("/horarios-medicamentos/{horarioId}/status")
    fun atualizarStatus(@PathVariable horarioId: Long, @RequestBody body: Map<String, String>): ResponseEntity<Any> {
        val horarioOpt = horarioRepo.findById(horarioId)
        if (horarioOpt.isEmpty) return ResponseEntity.notFound().build()

        val horario = horarioOpt.get()
        body["status"]?.let { horario.statusDose = it }

        horarioRepo.save(horario)
        return ResponseEntity.ok(horario)
    }
}