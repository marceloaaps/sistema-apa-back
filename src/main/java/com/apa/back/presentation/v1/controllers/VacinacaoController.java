package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.animal.VacinacaoUseCase;
import com.apa.back.presentation.v1.dtos.animal.VacinacaoDto;
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
@RequestMapping("/api/v1/vacinacoes")
@Tag(name = "Vacinações", description = "Endpoints para gerenciamento de vacinações dos animais")
public class VacinacaoController {

    private final VacinacaoUseCase vacinacaoUseCase;

    public VacinacaoController(VacinacaoUseCase vacinacaoUseCase) {
        this.vacinacaoUseCase = vacinacaoUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Criar novo registro de vacinação",
            description = "Registra uma nova vacinação aplicada em um animal. Inclui informações como nome da vacina, dose, data de aplicação, validade e veterinário responsável.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Vacinação registrada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VacinacaoDto.class)
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
    public ResponseEntity<VacinacaoDto> createVacinacao(
            @Parameter(description = "Dados da vacinação a ser registrada", required = true)
            @Valid @RequestBody VacinacaoDto dto) {
        VacinacaoDto created = vacinacaoUseCase.createVacinacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/animal/{animalId}")
    @Operation(
            summary = "Buscar vacinações por ID do animal",
            description = "Retorna todas as vacinações registradas para um animal específico, ordenadas por data de aplicação (mais recentes primeiro).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de vacinações retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = VacinacaoDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Animal não encontrado",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<List<VacinacaoDto>> getVacinacoesByAnimal(
            @Parameter(description = "ID do animal", required = true, example = "1")
            @PathVariable Long animalId) {
        List<VacinacaoDto> vacinacoes = vacinacaoUseCase.getVacinacoesByAnimal(animalId);
        return ResponseEntity.ok(vacinacoes);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar vacinação por ID",
            description = "Retorna uma vacinação específica pelo seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vacinação encontrada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VacinacaoDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vacinação não encontrada",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<VacinacaoDto> getVacinacaoById(
            @Parameter(description = "ID da vacinação", required = true, example = "1")
            @PathVariable Long id) {
        VacinacaoDto vacinacao = vacinacaoUseCase.getVacinacaoById(id);
        return ResponseEntity.ok(vacinacao);
    }

    @GetMapping("/vencidas")
    @Operation(
            summary = "Buscar vacinações vencidas",
            description = "Retorna todas as vacinações cuja data de validade já passou. Útil para identificar animais que precisam de reforço vacinal.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de vacinações vencidas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = VacinacaoDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<VacinacaoDto>> getVacinacoesVencidas() {
        List<VacinacaoDto> vencidas = vacinacaoUseCase.getVacinacoesVencidas();
        return ResponseEntity.ok(vencidas);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar registro de vacinação",
            description = "Atualiza os dados de uma vacinação existente. Útil para corrigir informações ou adicionar observações.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vacinação atualizada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = VacinacaoDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vacinação não encontrada",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos para atualização",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<VacinacaoDto> updateVacinacao(
            @Parameter(description = "ID da vacinação a ser atualizada", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados da vacinação", required = true)
            @Valid @RequestBody VacinacaoDto dto) {
        VacinacaoDto updated = vacinacaoUseCase.updateVacinacao(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar registro de vacinação",
            description = "Remove permanentemente um registro de vacinação. Use com cautela, pois esta operação não pode ser desfeita.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Vacinação deletada com sucesso",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vacinação não encontrada",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> deleteVacinacao(
            @Parameter(description = "ID da vacinação a ser deletada", required = true, example = "1")
            @PathVariable Long id) {
        vacinacaoUseCase.deleteVacinacao(id);
        return ResponseEntity.noContent().build();
    }
}
