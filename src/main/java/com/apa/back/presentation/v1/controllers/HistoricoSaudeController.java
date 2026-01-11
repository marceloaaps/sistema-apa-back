package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.animal.HistoricoSaudeUseCase;
import com.apa.back.presentation.v1.dtos.animal.HistoricoSaudeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/historico-saude")
@Tag(name = "Histórico de Saúde", description = "Endpoints para gerenciamento do histórico de saúde dos animais")
public class HistoricoSaudeController {

    private final HistoricoSaudeUseCase historicoSaudeUseCase;

    public HistoricoSaudeController(HistoricoSaudeUseCase historicoSaudeUseCase) {
        this.historicoSaudeUseCase = historicoSaudeUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Criar novo registro de histórico de saúde",
            description = "Cria um novo evento de saúde para um animal. Tipos de evento incluem: Doença, Deficiência, Histórico, Castração, Consulta, Cirurgia.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Histórico de saúde criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HistoricoSaudeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Animal não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos para criação",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<HistoricoSaudeDto> createHistorico(
            @Parameter(description = "Dados do histórico de saúde a ser criado", required = true)
            @Valid @RequestBody HistoricoSaudeDto dto) {
        HistoricoSaudeDto created = historicoSaudeUseCase.createHistoricoSaude(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/animal/{animalId}")
    @Operation(
            summary = "Buscar histórico de saúde por ID do animal",
            description = "Retorna todos os eventos de saúde registrados para um animal específico, ordenados por data do evento (mais recentes primeiro).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de eventos de saúde retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = HistoricoSaudeDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Animal não encontrado",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<List<HistoricoSaudeDto>> getHistoricoByAnimal(
            @Parameter(description = "ID do animal", required = true, example = "1")
            @PathVariable Long animalId) {
        List<HistoricoSaudeDto> historico = historicoSaudeUseCase.getHistoricoByAnimal(animalId);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar registro de histórico por ID",
            description = "Retorna um evento de saúde específico pelo seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Histórico de saúde encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HistoricoSaudeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Histórico de saúde não encontrado",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<HistoricoSaudeDto> getHistoricoById(
            @Parameter(description = "ID do histórico de saúde", required = true, example = "1")
            @PathVariable Long id) {
        HistoricoSaudeDto historico = historicoSaudeUseCase.getHistoricoById(id);
        return ResponseEntity.ok(historico);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar registro de histórico de saúde",
            description = "Atualiza um evento de saúde existente. A data do evento não é alterada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Histórico de saúde atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HistoricoSaudeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Histórico de saúde não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos para atualização",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<HistoricoSaudeDto> updateHistorico(
            @Parameter(description = "ID do histórico de saúde a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados do histórico de saúde", required = true)
            @Valid @RequestBody HistoricoSaudeDto dto) {
        HistoricoSaudeDto updated = historicoSaudeUseCase.updateHistoricoSaude(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar registro de histórico de saúde",
            description = "Remove permanentemente um evento de saúde do histórico.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Histórico de saúde deletado com sucesso",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Histórico de saúde não encontrado",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> deleteHistorico(
            @Parameter(description = "ID do histórico de saúde a ser deletado", required = true, example = "1")
            @PathVariable Long id) {
        historicoSaudeUseCase.deleteHistoricoSaude(id);
        return ResponseEntity.noContent().build();
    }
}
