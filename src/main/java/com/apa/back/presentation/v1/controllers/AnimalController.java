package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.animal.AnimalUseCase;
import com.apa.back.presentation.dtos.animal.AnimalDto;
import com.apa.back.presentation.dtos.animal.AnimalModel;
import com.apa.back.presentation.dtos.utils.PaginacaoDto;
import com.apa.back.presentation.v1.assemblers.AnimalModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals/v1")
public class AnimalController {

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
        AnimalDto savedAnimalDto = animalUseCase.createAnimal(animalDTO);
        AnimalModel model = AnimalModelAssembler.toModel(savedAnimalDto);
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
        AnimalDto updatedAnimalDto = animalUseCase.updateAnimal(id, animalDTO);
        if (updatedAnimalDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        AnimalModel model = AnimalModelAssembler.toModel(updatedAnimalDto);
        return ResponseEntity.ok(model);
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
        boolean deleted = animalUseCase.deleteAnimal(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
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
        AnimalDto restoredAnimalDto = animalUseCase.restoreAnimal(id);
        if (restoredAnimalDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        AnimalModel model = AnimalModelAssembler.toModel(restoredAnimalDto);
        return ResponseEntity.ok(model);
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
        AnimalDto animalDTO = animalUseCase.getAnimalById(id);
        if (animalDTO == null) {
            return ResponseEntity.notFound().build();
        }
        AnimalModel model = AnimalModelAssembler.toModel(animalDTO);
        return ResponseEntity.ok(model);
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
