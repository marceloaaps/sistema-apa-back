package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.use_cases.animal.AnimalUseCase;
import com.apa.back.presentation.dtos.AnimalDto;
import com.apa.back.presentation.dtos.PaginacaoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação",
                            content = @Content)
            }
    )
    @PostMapping("/create")
    public ResponseEntity<Animal> createAnimal(
            @Parameter(description = "Dados do animal para criação", required = true)
            @RequestBody AnimalDto animalDTO) {
        Animal savedAnimal = animalUseCase.createAnimal(animalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimal);
    }

    @Operation(
            summary = "Atualizar um animal existente",
            description = "Atualiza os dados do animal identificado pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado",
                            content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(
            @Parameter(description = "ID do animal a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novos dados do animal", required = true)
            @RequestBody AnimalDto animalDTO) {
        Animal updatedAnimal = animalUseCase.updateAnimal(id, animalDTO);
        return updatedAnimal != null ?
                ResponseEntity.status(HttpStatus.OK).body(updatedAnimal) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado para restauração",
                            content = @Content)
            }
    )
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Animal> restoreAnimal(
            @Parameter(description = "ID do animal a ser restaurado", required = true)
            @PathVariable Long id) {
        Animal restoredAnimal = animalUseCase.restoreAnimal(id);
        return restoredAnimal != null ?
                ResponseEntity.status(HttpStatus.OK).body(restoredAnimal) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
            summary = "Buscar animal por ID",
            description = "Retorna os dados do animal identificado pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Animal encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnimalDto.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> getAnimalById(
            @Parameter(description = "ID do animal para busca", required = true)
            @PathVariable Long id) {
        AnimalDto animalDTO = animalUseCase.getAnimalById(id);
        return animalDTO != null ?
                ResponseEntity.ok(animalDTO) :
                ResponseEntity.notFound().build();
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
    public ResponseEntity<PaginacaoDto<AnimalDto>> getAnimaisDisponiveis(
            @Parameter(description = "Parâmetros de paginação")
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AnimalDto> page = animalUseCase.getAnimaisDisponiveis(pageable);
        return ResponseEntity.ok(new PaginacaoDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        ));
    }

}
