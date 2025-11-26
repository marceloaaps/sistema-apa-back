package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.animal.VacinacaoUseCase;
import com.apa.back.presentation.v1.dtos.animal.VacinacaoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Operation(summary = "Criar novo registro de vacinação")
    public ResponseEntity<VacinacaoDto> createVacinacao(@RequestBody VacinacaoDto dto) {
        VacinacaoDto created = vacinacaoUseCase.createVacinacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/animal/{animalId}")
    @Operation(summary = "Buscar vacinações por ID do animal")
    public ResponseEntity<List<VacinacaoDto>> getVacinacoesByAnimal(@PathVariable Long animalId) {
        List<VacinacaoDto> vacinacoes = vacinacaoUseCase.getVacinacoesByAnimal(animalId);
        return ResponseEntity.ok(vacinacoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vacinação por ID")
    public ResponseEntity<VacinacaoDto> getVacinacaoById(@PathVariable Long id) {
        VacinacaoDto vacinacao = vacinacaoUseCase.getVacinacaoById(id);
        return ResponseEntity.ok(vacinacao);
    }

    @GetMapping("/vencidas")
    @Operation(summary = "Buscar vacinações vencidas")
    public ResponseEntity<List<VacinacaoDto>> getVacinacoesVencidas() {
        List<VacinacaoDto> vencidas = vacinacaoUseCase.getVacinacoesVencidas();
        return ResponseEntity.ok(vencidas);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro de vacinação")
    public ResponseEntity<VacinacaoDto> updateVacinacao(@PathVariable Long id,
                                                        @RequestBody VacinacaoDto dto) {
        VacinacaoDto updated = vacinacaoUseCase.updateVacinacao(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar registro de vacinação")
    public ResponseEntity<Void> deleteVacinacao(@PathVariable Long id) {
        vacinacaoUseCase.deleteVacinacao(id);
        return ResponseEntity.noContent().build();
    }
}

