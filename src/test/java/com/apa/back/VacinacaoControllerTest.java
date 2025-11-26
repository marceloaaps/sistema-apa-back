package com.apa.back;

import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.core.use_cases.animal.VacinacaoUseCase;
import com.apa.back.presentation.v1.controllers.VacinacaoController;
import com.apa.back.presentation.v1.dtos.animal.VacinacaoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VacinacaoControllerTest {

    @Mock
    private VacinacaoUseCase vacinacaoUseCase;

    @InjectMocks
    private VacinacaoController vacinacaoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private VacinacaoDto criarVacinacaoDtoExemplo() {
        return VacinacaoDto.builder()
                .id(1L)
                .idAnimal(100L)
                .nomeVacina("V10")
                .dataAplicacao(LocalDate.of(2024, 1, 15))
                .dose("1ª dose")
                .validade(LocalDate.of(2025, 1, 15))
                .veterinario("Dr. João Silva")
                .observacoes("Animal tolerou bem a vacina")
                .build();
    }

    @Test
    void createVacinacao_deveRetornarCreatedComVacinacao() {
        VacinacaoDto dtoEntrada = criarVacinacaoDtoExemplo();
        when(vacinacaoUseCase.createVacinacao(any(VacinacaoDto.class)))
                .thenReturn(dtoEntrada);

        ResponseEntity<VacinacaoDto> response = vacinacaoController.createVacinacao(dtoEntrada);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(100L, response.getBody().getIdAnimal());
        assertEquals("V10", response.getBody().getNomeVacina());
        assertEquals("1ª dose", response.getBody().getDose());
        assertEquals("Dr. João Silva", response.getBody().getVeterinario());
        assertEquals("Animal tolerou bem a vacina", response.getBody().getObservacoes());
        verify(vacinacaoUseCase, times(1)).createVacinacao(dtoEntrada);
    }

    @Test
    void createVacinacao_deveRetornarCreatedComDiferentesVacinas() {
        VacinacaoDto dtoRaiva = VacinacaoDto.builder()
                .id(2L)
                .idAnimal(100L)
                .nomeVacina("Antirrábica")
                .dataAplicacao(LocalDate.now())
                .dose("Dose única")
                .validade(LocalDate.now().plusYears(1))
                .veterinario("Dra. Maria Santos")
                .observacoes("Reforço anual")
                .build();

        when(vacinacaoUseCase.createVacinacao(any(VacinacaoDto.class)))
                .thenReturn(dtoRaiva);

        ResponseEntity<VacinacaoDto> response = vacinacaoController.createVacinacao(dtoRaiva);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Antirrábica", response.getBody().getNomeVacina());
        assertEquals("Dose única", response.getBody().getDose());
        verify(vacinacaoUseCase, times(1)).createVacinacao(dtoRaiva);
    }

    @Test
    void getVacinacoesByAnimal_deveRetornarListaDeVacinacoes() {
        Long animalId = 100L;
        List<VacinacaoDto> vacinacoes = Arrays.asList(
                criarVacinacaoDtoExemplo(),
                VacinacaoDto.builder()
                        .id(2L)
                        .idAnimal(animalId)
                        .nomeVacina("Antirrábica")
                        .dataAplicacao(LocalDate.of(2024, 2, 10))
                        .dose("Dose única")
                        .validade(LocalDate.of(2025, 2, 10))
                        .veterinario("Dra. Maria Santos")
                        .observacoes("Sem intercorrências")
                        .build()
        );

        when(vacinacaoUseCase.getVacinacoesByAnimal(animalId)).thenReturn(vacinacoes);

        ResponseEntity<List<VacinacaoDto>> response =
                vacinacaoController.getVacinacoesByAnimal(animalId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("V10", response.getBody().get(0).getNomeVacina());
        assertEquals("Antirrábica", response.getBody().get(1).getNomeVacina());
        verify(vacinacaoUseCase, times(1)).getVacinacoesByAnimal(animalId);
    }

    @Test
    void getVacinacoesByAnimal_deveRetornarListaVaziaQuandoNaoHouverVacinacoes() {
        Long animalId = 100L;
        when(vacinacaoUseCase.getVacinacoesByAnimal(animalId)).thenReturn(List.of());

        ResponseEntity<List<VacinacaoDto>> response =
                vacinacaoController.getVacinacoesByAnimal(animalId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(vacinacaoUseCase, times(1)).getVacinacoesByAnimal(animalId);
    }

    @Test
    void getVacinacaoById_deveRetornarVacinacaoQuandoEncontrar() {
        VacinacaoDto dto = criarVacinacaoDtoExemplo();
        when(vacinacaoUseCase.getVacinacaoById(1L)).thenReturn(dto);

        ResponseEntity<VacinacaoDto> response = vacinacaoController.getVacinacaoById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("V10", response.getBody().getNomeVacina());
        verify(vacinacaoUseCase, times(1)).getVacinacaoById(1L);
    }

    @Test
    void getVacinacaoById_deveLancarExcecaoQuandoNaoEncontrar() {
        when(vacinacaoUseCase.getVacinacaoById(999L))
                .thenThrow(new DomainNotFoundException("Vacinação não encontrada: ID 999"));

        assertThrows(DomainNotFoundException.class, () -> {
            vacinacaoController.getVacinacaoById(999L);
        });

        verify(vacinacaoUseCase, times(1)).getVacinacaoById(999L);
    }

    @Test
    void getVacinacoesVencidas_deveRetornarListaDeVacinacoesVencidas() {
        List<VacinacaoDto> vacinacoesVencidas = Arrays.asList(
                VacinacaoDto.builder()
                        .id(1L)
                        .idAnimal(100L)
                        .nomeVacina("V10")
                        .dataAplicacao(LocalDate.of(2023, 1, 15))
                        .dose("1ª dose")
                        .validade(LocalDate.of(2024, 1, 15))
                        .veterinario("Dr. João Silva")
                        .observacoes("Precisa renovar")
                        .build(),
                VacinacaoDto.builder()
                        .id(2L)
                        .idAnimal(101L)
                        .nomeVacina("Antirrábica")
                        .dataAplicacao(LocalDate.of(2022, 6, 10))
                        .dose("Dose única")
                        .validade(LocalDate.of(2023, 6, 10))
                        .veterinario("Dra. Maria Santos")
                        .observacoes("Vencida")
                        .build()
        );

        when(vacinacaoUseCase.getVacinacoesVencidas()).thenReturn(vacinacoesVencidas);

        ResponseEntity<List<VacinacaoDto>> response = vacinacaoController.getVacinacoesVencidas();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().get(0).getValidade().isBefore(LocalDate.now()));
        assertTrue(response.getBody().get(1).getValidade().isBefore(LocalDate.now()));
        verify(vacinacaoUseCase, times(1)).getVacinacoesVencidas();
    }

    @Test
    void getVacinacoesVencidas_deveRetornarListaVaziaQuandoNaoHouverVencidas() {
        when(vacinacaoUseCase.getVacinacoesVencidas()).thenReturn(List.of());

        ResponseEntity<List<VacinacaoDto>> response = vacinacaoController.getVacinacoesVencidas();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(vacinacaoUseCase, times(1)).getVacinacoesVencidas();
    }

    @Test
    void updateVacinacao_deveRetornarOkComVacinacaoAtualizada() {
        Long id = 1L;
        VacinacaoDto dtoEntrada = criarVacinacaoDtoExemplo();
        dtoEntrada.setObservacoes("Observações atualizadas");
        dtoEntrada.setDose("2ª dose");

        when(vacinacaoUseCase.updateVacinacao(eq(id), any(VacinacaoDto.class)))
                .thenReturn(dtoEntrada);

        ResponseEntity<VacinacaoDto> response =
                vacinacaoController.updateVacinacao(id, dtoEntrada);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Observações atualizadas", response.getBody().getObservacoes());
        assertEquals("2ª dose", response.getBody().getDose());
        verify(vacinacaoUseCase, times(1)).updateVacinacao(id, dtoEntrada);
    }

    @Test
    void updateVacinacao_deveLancarExcecaoQuandoNaoEncontrar() {
        Long id = 999L;
        VacinacaoDto dtoEntrada = criarVacinacaoDtoExemplo();

        when(vacinacaoUseCase.updateVacinacao(eq(id), any(VacinacaoDto.class)))
                .thenThrow(new DomainNotFoundException("Vacinação não encontrada: ID 999"));

        assertThrows(DomainNotFoundException.class, () -> {
            vacinacaoController.updateVacinacao(id, dtoEntrada);
        });

        verify(vacinacaoUseCase, times(1)).updateVacinacao(eq(id), any());
    }

    @Test
    void deleteVacinacao_deveRetornarNoContentQuandoExcluirComSucesso() {
        Long id = 1L;
        doNothing().when(vacinacaoUseCase).deleteVacinacao(id);

        ResponseEntity<Void> response = vacinacaoController.deleteVacinacao(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(vacinacaoUseCase, times(1)).deleteVacinacao(id);
    }

    @Test
    void deleteVacinacao_deveLancarExcecaoQuandoNaoEncontrar() {
        Long id = 999L;
        doThrow(new DomainNotFoundException("Vacinação não encontrada: ID 999"))
                .when(vacinacaoUseCase).deleteVacinacao(id);

        assertThrows(DomainNotFoundException.class, () -> {
            vacinacaoController.deleteVacinacao(id);
        });

        verify(vacinacaoUseCase, times(1)).deleteVacinacao(id);
    }

    @Test
    void createVacinacao_deveAceitarVacinacaoSemObservacoes() {
        VacinacaoDto dtoSemObservacoes = VacinacaoDto.builder()
                .id(3L)
                .idAnimal(100L)
                .nomeVacina("V8")
                .dataAplicacao(LocalDate.now())
                .dose("1ª dose")
                .validade(LocalDate.now().plusYears(1))
                .veterinario("Dr. Carlos")
                .observacoes(null)
                .build();

        when(vacinacaoUseCase.createVacinacao(any(VacinacaoDto.class)))
                .thenReturn(dtoSemObservacoes);

        ResponseEntity<VacinacaoDto> response = vacinacaoController.createVacinacao(dtoSemObservacoes);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getObservacoes());
        verify(vacinacaoUseCase, times(1)).createVacinacao(dtoSemObservacoes);
    }

    @Test
    void createVacinacao_deveValidarDataAplicacaoEValidade() {
        VacinacaoDto dto = VacinacaoDto.builder()
                .id(4L)
                .idAnimal(100L)
                .nomeVacina("Polivalente")
                .dataAplicacao(LocalDate.of(2024, 3, 15))
                .dose("1ª dose")
                .validade(LocalDate.of(2025, 3, 15))
                .veterinario("Dra. Ana")
                .observacoes("Validade de 1 ano")
                .build();

        when(vacinacaoUseCase.createVacinacao(any(VacinacaoDto.class)))
                .thenReturn(dto);

        ResponseEntity<VacinacaoDto> response = vacinacaoController.createVacinacao(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getValidade().isAfter(response.getBody().getDataAplicacao()));
        verify(vacinacaoUseCase, times(1)).createVacinacao(dto);
    }
}

