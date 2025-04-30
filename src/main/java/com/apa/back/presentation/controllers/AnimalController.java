package com.apa.back.presentation.controllers;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.use_cases.animal.AnimalUseCase;
import com.apa.back.presentation.dtos.AnimalDTO;
import com.apa.back.presentation.dtos.PaginacaoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalUseCase animalUseCase;

    public AnimalController(AnimalUseCase animalUseCase) {
        this.animalUseCase = animalUseCase;
    }

    @PostMapping
    public ResponseEntity<Animal> createAnimal(@RequestBody AnimalDTO animalDTO) {
        Animal savedAnimal = animalUseCase.createAnimal(animalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable Long id, @RequestBody AnimalDTO animalDTO) {
        Animal updatedAnimal = animalUseCase.updateAnimal(id, animalDTO);
        return updatedAnimal != null ?
                ResponseEntity.status(HttpStatus.OK).body(updatedAnimal) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id, @RequestParam("deletadoPor") Integer deletadoPor) {
        boolean deleted = animalUseCase.deleteAnimal(id, deletadoPor);
        return deleted ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Animal> restoreAnimal(@PathVariable Long id) {
        Animal restoredAnimal = animalUseCase.restoreAnimal(id);
        return restoredAnimal != null ?
                ResponseEntity.status(HttpStatus.OK).body(restoredAnimal) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> getAnimalById(@PathVariable Long id) {
        AnimalDTO animalDTO = animalUseCase.getAnimalById(id);
        return animalDTO != null ?
                ResponseEntity.ok(animalDTO) :
                ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<PaginacaoDto<AnimalDTO>> getAnimaisDisponiveis(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AnimalDTO> page = animalUseCase.getAnimaisDisponiveis(pageable);
        return ResponseEntity.ok(new PaginacaoDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        ));
    }

}