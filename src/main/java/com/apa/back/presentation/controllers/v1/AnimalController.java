package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.use_cases.animal.AnimalUseCase;
import com.apa.back.presentation.dtos.AnimalDto;
import com.apa.back.presentation.dtos.PaginacaoDto;
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

    @PostMapping("/create")
    public ResponseEntity<Animal> createAnimal(@RequestBody AnimalDto animalDTO) {
        Animal savedAnimal = animalUseCase.createAnimal(animalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable Long id, @RequestBody AnimalDto animalDTO) {
        Animal updatedAnimal = animalUseCase.updateAnimal(id, animalDTO);
        return updatedAnimal != null ?
                ResponseEntity.status(HttpStatus.OK).body(updatedAnimal) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        boolean deleted = animalUseCase.deleteAnimal(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


    @PatchMapping("/{id}/restore")
    public ResponseEntity<Animal> restoreAnimal(@PathVariable Long id) {
        Animal restoredAnimal = animalUseCase.restoreAnimal(id);
        return restoredAnimal != null ?
                ResponseEntity.status(HttpStatus.OK).body(restoredAnimal) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> getAnimalById(@PathVariable Long id) {
        AnimalDto animalDTO = animalUseCase.getAnimalById(id);
        return animalDTO != null ?
                ResponseEntity.ok(animalDTO) :
                ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<PaginacaoDto<AnimalDto>> getAnimaisDisponiveis(
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