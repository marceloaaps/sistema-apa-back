package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.animal.HistoricoSaudeUseCase;
import com.apa.back.presentation.v1.dtos.animal.HistoricoSaudeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historico-saude")
@Tag(name = "Histórico de Saúde", description = "Endpoints para gerenciamento do histórico de saúde dos animais")
public class HistoricoSaudeController {

    private final HistoricoSaudeUseCase historicoSaudeUseCase;

    public HistoricoSaudeController(HistoricoSaudeUseCase historicoSaudeUseCase) {
        this.historicoSaudeUseCase = historicoSaudeUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar novo registro de histórico de saúde")
    public ResponseEntity<HistoricoSaudeDto> createHistorico(@RequestBody HistoricoSaudeDto dto) {
        HistoricoSaudeDto created = historicoSaudeUseCase.createHistoricoSaude(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/animal/{animalId}")
    @Operation(summary = "Buscar histórico de saúde por ID do animal")
    public ResponseEntity<List<HistoricoSaudeDto>> getHistoricoByAnimal(@PathVariable Long animalId) {
        List<HistoricoSaudeDto> historico = historicoSaudeUseCase.getHistoricoByAnimal(animalId);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar registro de histórico por ID")
    public ResponseEntity<HistoricoSaudeDto> getHistoricoById(@PathVariable Long id) {
        HistoricoSaudeDto historico = historicoSaudeUseCase.getHistoricoById(id);
        return ResponseEntity.ok(historico);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro de histórico de saúde")
    public ResponseEntity<HistoricoSaudeDto> updateHistorico(@PathVariable Long id,
                                                              @RequestBody HistoricoSaudeDto dto) {
        HistoricoSaudeDto updated = historicoSaudeUseCase.updateHistoricoSaude(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar registro de histórico de saúde")
    public ResponseEntity<Void> deleteHistorico(@PathVariable Long id) {
        historicoSaudeUseCase.deleteHistoricoSaude(id);
        return ResponseEntity.noContent().build();
    }
}

