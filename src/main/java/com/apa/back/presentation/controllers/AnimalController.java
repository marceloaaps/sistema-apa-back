package com.apa.back.presentation.controllers;

import com.apa.back.core.domain.entities.Animais;
import com.apa.back.core.use_cases.AnimalUseCase;
import com.apa.back.presentation.dtos.AnimalDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalUseCase animalUseCase;

    public AnimalController(AnimalUseCase animalUseCase) {
        this.animalUseCase = animalUseCase;
    }

    @PostMapping
    public ResponseEntity<Animais> createAnimal(@RequestBody AnimalDTO animalDTO) {
        Animais savedAnimal = animalUseCase.createAnimal(animalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animais> updateAnimal(@PathVariable Long id, @RequestBody AnimalDTO animalDTO) {
        Animais updatedAnimal = animalUseCase.updateAnimal(id, animalDTO);
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
    public ResponseEntity<Animais> restoreAnimal(@PathVariable Long id) {
        Animais restoredAnimal = animalUseCase.restoreAnimal(id);
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
    public ResponseEntity<List<AnimalDTO>> getAnimaisDisponiveis() {
        List<AnimalDTO> animaisDisponiveis = animalUseCase.getAnimaisDisponiveis();
        return ResponseEntity.ok(animaisDisponiveis);
    }
}
