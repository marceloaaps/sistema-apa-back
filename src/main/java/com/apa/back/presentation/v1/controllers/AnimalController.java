package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.core.use_cases.animal.AnimalUseCase;
import com.apa.back.presentation.v1.dtos.animal.AnimalDto;
import com.apa.back.presentation.v1.dtos.animal.AnimalModel;
import com.apa.back.presentation.v1.dtos.utils.PaginacaoDto;
import com.apa.back.presentation.v1.assemblers.AnimalModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {

    private static final Logger logger = LogManager.getLogger(AnimalController.class);

    private final AnimalUseCase animalUseCase;

    public AnimalController(AnimalUseCase animalUseCase) {
        this.animalUseCase = animalUseCase;
    }

    @Operation(
            summary = "Criar um novo animal",
            description = "Cria um novo registro de animal com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Animal criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnimalModel.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação",
                            content = @Content)
            }
    )
    @PostMapping("/create")
    public ResponseEntity<AnimalModel> createAnimal(
            @Parameter(description = "Dados do animal para criação", required = true)
            @RequestBody AnimalDto animalDTO) {
        logger.info("Requisição para criar animal: {}", animalDTO.getNome());
        AnimalDto savedAnimalDto = animalUseCase.createAnimal(animalDTO);
        AnimalModel model = AnimalModelAssembler.toModel(savedAnimalDto);
        logger.info("Animal criado com sucesso via API - ID: {}", savedAnimalDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(
            summary = "Atualizar um animal existente",
            description = "Atualiza os dados do animal identificado pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnimalModel.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado",
                            content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AnimalModel> updateAnimal(
            @Parameter(description = "ID do animal a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novos dados do animal", required = true)
            @RequestBody AnimalDto animalDTO) {
        logger.info("Requisição para atualizar animal ID: {}", id);
        try {
            AnimalDto updatedAnimalDto = animalUseCase.updateAnimal(id, animalDTO);
            AnimalModel model = AnimalModelAssembler.toModel(updatedAnimalDto);
            logger.info("Animal atualizado com sucesso via API - ID: {}", id);
            return ResponseEntity.ok(model);
        } catch (DomainNotFoundException ex) {
            logger.warn("Animal não encontrado para atualização via API - ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "Excluir um animal",
            description = "Exclui o animal identificado pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Animal excluído com sucesso",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(
            @Parameter(description = "ID do animal a ser excluído", required = true)
            @PathVariable Long id) {
        logger.info("Requisição para deletar animal ID: {}", id);
        try {
            animalUseCase.deleteAnimal(id);
            logger.info("Animal deletado com sucesso via API - ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (DomainNotFoundException ex) {
            logger.warn("Animal não encontrado para exclusão via API - ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Restaurar animal excluído",
            description = "Restaura um animal previamente excluído identificado pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal restaurado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnimalModel.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado para restauração",
                            content = @Content)
            }
    )
    @PatchMapping("/{id}/restore")
    public ResponseEntity<AnimalModel> restoreAnimal(
            @Parameter(description = "ID do animal a ser restaurado", required = true)
            @PathVariable Long id) {
        try {
            AnimalDto restoredAnimalDto = animalUseCase.restoreAnimal(id);
            AnimalModel model = AnimalModelAssembler.toModel(restoredAnimalDto);
            return ResponseEntity.ok(model);
        } catch (DomainNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "Buscar animal por ID",
            description = "Retorna os dados do animal identificado pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnimalModel.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AnimalModel> getAnimalById(@PathVariable Long id) {
        try {
            AnimalDto animalDTO = animalUseCase.getAnimalById(id);
            AnimalModel model = AnimalModelAssembler.toModel(animalDTO);
            return ResponseEntity.ok(model);
        } catch (DomainNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Listar animais disponíveis com paginação",
            description = "Retorna uma lista paginada dos animais disponíveis para adoção ou consulta.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista paginada de animais retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaginacaoDto.class)))
            }
    )

    @GetMapping
    public ResponseEntity<PagedModel<AnimalModel>> getAnimaisDisponiveis(
            @PageableDefault(size = 20) Pageable pageable,
            PagedResourcesAssembler<AnimalDto> pagedResourcesAssembler) {

        Page<AnimalDto> page = animalUseCase.getAnimaisDisponiveis(pageable);
        PagedModel<AnimalModel> pagedModel = pagedResourcesAssembler.toModel(page, AnimalModelAssembler::toModel);
        return ResponseEntity.ok(pagedModel);
    }


}
