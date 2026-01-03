package com.apa.back;

import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.core.use_cases.animal.AnimalUseCase;
import com.apa.back.presentation.v1.controllers.AnimalController;
import com.apa.back.presentation.v1.dtos.animal.AnimalDto;
import com.apa.back.presentation.v1.dtos.animal.AnimalModel;
import com.apa.back.presentation.v1.dtos.animal.HistoricoSaudeDto;
import com.apa.back.presentation.v1.dtos.animal.VacinacaoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AnimalControllerTest {

    @Mock
    private AnimalUseCase animalUseCase;

    @Mock
    private PagedResourcesAssembler<AnimalDto> pagedResourcesAssembler;

    @InjectMocks
    private AnimalController animalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private AnimalDto criarAnimalDtoExemplo() {
        return new AnimalDto(
                1L,
                "Rex",
                3,
                "Labrador",
                "RG-2024-001",
                "Cachorro",
                "M",
                "Marrom",
                "Calmo e amigável",
                "Encontrado na rua, saudável",
                LocalDate.of(2023, 4, 1),
                true,
                null,
                null,
                new java.util.ArrayList<>(),
                new java.util.ArrayList<>()
        );
    }

    private HistoricoSaudeDto criarHistoricoSaudeDtoExemplo() {
        return HistoricoSaudeDto.builder()
                .id(1L)
                .idAnimal(1L)
                .tipoEvento("Consulta")
                .descricao("Consulta de rotina. Animal em boas condições de saúde.")
                .castrado(false)
                .dataEvento(LocalDateTime.of(2024, 1, 15, 10, 30))
                .build();
    }

    private VacinacaoDto criarVacinacaoDtoExemplo() {
        return VacinacaoDto.builder()
                .id(1L)
                .idAnimal(1L)
                .nomeVacina("V10")
                .dataAplicacao(LocalDate.of(2024, 1, 15))
                .dose("1ª dose")
                .validade(LocalDate.of(2025, 1, 15))
                .veterinario("Dr. João Silva")
                .observacoes("Animal tolerou bem a vacina.")
                .build();
    }

    private AnimalDto criarAnimalDtoCompletoExemplo() {
        List<HistoricoSaudeDto> historicoSaude = new ArrayList<>();
        historicoSaude.add(criarHistoricoSaudeDtoExemplo());

        List<VacinacaoDto> vacinacoes = new ArrayList<>();
        vacinacoes.add(criarVacinacaoDtoExemplo());

        return new AnimalDto(
                1L,
                "Rex",
                3,
                "Labrador",
                "RG-2024-001",
                "Cachorro",
                "M",
                "Marrom",
                "Calmo e amigável",
                "Encontrado na rua, saudável",
                LocalDate.of(2023, 4, 1),
                true,
                null,
                null,
                historicoSaude,
                vacinacoes
        );
    }

    private AnimalDto criarAnimalDtoDeletadoExemplo() {
        return new AnimalDto(
                1L,
                "Rex",
                3,
                "Labrador",
                "RG-2024-001",
                "Cachorro",
                "M",
                "Marrom",
                "Calmo e amigável",
                "Encontrado na rua, saudável",
                LocalDate.of(2023, 4, 1),
                false,
                LocalDate.of(2024, 5, 15),
                42,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Test
    void createAnimal_deveRetornarCreatedComAnimalModel() {
        AnimalDto dtoEntrada = criarAnimalDtoExemplo();
        when(animalUseCase.createAnimal(any(AnimalDto.class))).thenReturn(dtoEntrada);

        ResponseEntity<AnimalModel> response = animalController.createAnimal(dtoEntrada);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(dtoEntrada.getId(), response.getBody().getAnimalDto().getId());
        assertEquals("Rex", response.getBody().getAnimalDto().getNome());
        assertEquals("RG-2024-001", response.getBody().getAnimalDto().getRgAnimal());
        assertEquals("Cachorro", response.getBody().getAnimalDto().getEspecie());
        assertEquals("M", response.getBody().getAnimalDto().getSexo());
        assertEquals("Marrom", response.getBody().getAnimalDto().getCor());
        verify(animalUseCase, times(1)).createAnimal(dtoEntrada);
    }

    @Test
    void updateAnimal_deveRetornarOkQuandoAnimalExistir() {
        AnimalDto dtoEntrada = criarAnimalDtoExemplo();
        when(animalUseCase.updateAnimal(eq(1L), any(AnimalDto.class))).thenReturn(dtoEntrada);

        ResponseEntity<AnimalModel> response = animalController.updateAnimal(1L, dtoEntrada);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Labrador", response.getBody().getAnimalDto().getRaca());
        verify(animalUseCase).updateAnimal(1L, dtoEntrada);
    }

    @Test
    void updateAnimal_deveRetornarNotFoundQuandoAnimalNaoExistir() {
        doThrow(new DomainNotFoundException("Animal não encontrado: ID 999"))
            .when(animalUseCase).updateAnimal(eq(999L), any(AnimalDto.class));

        ResponseEntity<AnimalModel> response = animalController.updateAnimal(999L, criarAnimalDtoExemplo());

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(animalUseCase).updateAnimal(eq(999L), any());
    }

    @Test
    void deleteAnimal_deveRetornarNoContentQuandoExcluirComSucesso() {
        doNothing().when(animalUseCase).deleteAnimal(1L);

        ResponseEntity<Void> response = animalController.deleteAnimal(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(animalUseCase).deleteAnimal(1L);
    }

    @Test
    void deleteAnimal_deveRetornarNotFoundQuandoNaoExcluir() {
        doThrow(new DomainNotFoundException("Animal não encontrado: ID 999"))
            .when(animalUseCase).deleteAnimal(999L);

        ResponseEntity<Void> response = animalController.deleteAnimal(999L);

        assertEquals(404, response.getStatusCodeValue());
        verify(animalUseCase).deleteAnimal(999L);
    }

    @Test
    void restoreAnimal_deveRetornarOkQuandoRestaurarComSucesso() {
        AnimalDto dto = criarAnimalDtoExemplo();
        when(animalUseCase.restoreAnimal(1L)).thenReturn(dto);

        ResponseEntity<AnimalModel> response = animalController.restoreAnimal(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getAnimalDto().getDisponivelParaAdocao());
        verify(animalUseCase).restoreAnimal(1L);
    }

    @Test
    void restoreAnimal_deveRetornarNotFoundQuandoNaoRestaurar() {
        doThrow(new DomainNotFoundException("Animal não encontrado: ID 999"))
            .when(animalUseCase).restoreAnimal(999L);

        ResponseEntity<AnimalModel> response = animalController.restoreAnimal(999L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(animalUseCase).restoreAnimal(999L);
    }

    @Test
    void getAnimalById_deveRetornarOkQuandoEncontrar() {
        AnimalDto dto = criarAnimalDtoExemplo();
        when(animalUseCase.getAnimalById(1L)).thenReturn(dto);

        ResponseEntity<AnimalModel> response = animalController.getAnimalById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getAnimalDto().getId());
        verify(animalUseCase).getAnimalById(1L);
    }

    @Test
    void getAnimalById_deveRetornarNotFoundQuandoNaoEncontrar() {
        doThrow(new DomainNotFoundException("Animal não encontrado: ID 999"))
            .when(animalUseCase).getAnimalById(999L);

        ResponseEntity<AnimalModel> response = animalController.getAnimalById(999L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(animalUseCase).getAnimalById(999L);
    }

    @Test
    void getAnimaisDisponiveis_deveRetornarOkComPagedModel() {
        AnimalDto dto = criarAnimalDtoExemplo();
        Pageable pageable = PageRequest.of(0, 20);
        Page<AnimalDto> page = new PageImpl<>(Collections.singletonList(dto));

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
            page.getSize(),
            page.getNumber(),
            page.getTotalElements()
        );
        PagedModel<AnimalModel> pagedModel = PagedModel.of(Collections.emptyList(), metadata);

        when(animalUseCase.getAnimaisDisponiveis(pageable)).thenReturn(page);
        when(pagedResourcesAssembler.toModel(
                eq(page),
                ArgumentMatchers.<RepresentationModelAssembler<AnimalDto, AnimalModel>>any())
        ).thenReturn(pagedModel);

        ResponseEntity<PagedModel<AnimalModel>> response = animalController.getAnimaisDisponiveis(
            pageable,
            pagedResourcesAssembler
        );

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(animalUseCase).getAnimaisDisponiveis(pageable);
        verify(pagedResourcesAssembler).toModel(
                eq(page),
                ArgumentMatchers.<RepresentationModelAssembler<AnimalDto, AnimalModel>>any()
        );
    }

    @Test
    void createAnimal_comHistoricoESaudeVacinacoes_deveRetornarCreatedComDadosCompletos() {
        AnimalDto dtoCompleto = criarAnimalDtoCompletoExemplo();
        when(animalUseCase.createAnimal(any(AnimalDto.class))).thenReturn(dtoCompleto);

        ResponseEntity<AnimalModel> response = animalController.createAnimal(dtoCompleto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        AnimalDto resultado = response.getBody().getAnimalDto();
        assertEquals(dtoCompleto.getId(), resultado.getId());
        assertNotNull(resultado.getHistoricoSaude());
        assertEquals(1, resultado.getHistoricoSaude().size());
        assertEquals("Consulta", resultado.getHistoricoSaude().get(0).getTipoEvento());
        assertNotNull(resultado.getVacinacoes());
        assertEquals(1, resultado.getVacinacoes().size());
        assertEquals("V10", resultado.getVacinacoes().get(0).getNomeVacina());
        verify(animalUseCase, times(1)).createAnimal(dtoCompleto);
    }

    @Test
    void getAnimalById_comHistoricoSaude_deveRetornarDadosCompletos() {
        AnimalDto dtoCompleto = criarAnimalDtoCompletoExemplo();
        when(animalUseCase.getAnimalById(1L)).thenReturn(dtoCompleto);

        ResponseEntity<AnimalModel> response = animalController.getAnimalById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        AnimalDto resultado = response.getBody().getAnimalDto();
        assertNotNull(resultado.getHistoricoSaude());
        assertFalse(resultado.getHistoricoSaude().isEmpty());
        assertEquals(1L, resultado.getHistoricoSaude().get(0).getId());
        assertEquals("Consulta de rotina. Animal em boas condições de saúde.",
                     resultado.getHistoricoSaude().get(0).getDescricao());
        assertEquals(false, resultado.getHistoricoSaude().get(0).getCastrado());
        verify(animalUseCase).getAnimalById(1L);
    }

    @Test
    void getAnimalById_comVacinacoes_deveRetornarDadosCompletos() {
        AnimalDto dtoCompleto = criarAnimalDtoCompletoExemplo();
        when(animalUseCase.getAnimalById(1L)).thenReturn(dtoCompleto);

        ResponseEntity<AnimalModel> response = animalController.getAnimalById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        AnimalDto resultado = response.getBody().getAnimalDto();
        assertNotNull(resultado.getVacinacoes());
        assertFalse(resultado.getVacinacoes().isEmpty());
        assertEquals(1L, resultado.getVacinacoes().get(0).getId());
        assertEquals("V10", resultado.getVacinacoes().get(0).getNomeVacina());
        assertEquals("1ª dose", resultado.getVacinacoes().get(0).getDose());
        assertEquals("Dr. João Silva", resultado.getVacinacoes().get(0).getVeterinario());
        assertEquals(LocalDate.of(2024, 1, 15), resultado.getVacinacoes().get(0).getDataAplicacao());
        assertEquals(LocalDate.of(2025, 1, 15), resultado.getVacinacoes().get(0).getValidade());
        verify(animalUseCase).getAnimalById(1L);
    }

    @Test
    void deleteAnimal_deveSoftDeleteComDeletadoEmEDeletadoPor() {
        AnimalDto dtoDeletado = criarAnimalDtoDeletadoExemplo();
        when(animalUseCase.getAnimalById(1L)).thenReturn(dtoDeletado);

        ResponseEntity<AnimalModel> response = animalController.getAnimalById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        AnimalDto resultado = response.getBody().getAnimalDto();
        assertFalse(resultado.getDisponivelParaAdocao());
        assertNotNull(resultado.getDeletadoEm());
        assertEquals(LocalDate.of(2024, 5, 15), resultado.getDeletadoEm());
        assertNotNull(resultado.getDeletadoPor());
        assertEquals(42, resultado.getDeletadoPor());
        verify(animalUseCase).getAnimalById(1L);
    }

    @Test
    void updateAnimal_comHistoricoSaudeVazio_deveAceitarListaVazia() {
        AnimalDto dtoComListasVazias = criarAnimalDtoExemplo();
        when(animalUseCase.updateAnimal(eq(1L), any(AnimalDto.class))).thenReturn(dtoComListasVazias);

        ResponseEntity<AnimalModel> response = animalController.updateAnimal(1L, dtoComListasVazias);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        AnimalDto resultado = response.getBody().getAnimalDto();
        assertNotNull(resultado.getHistoricoSaude());
        assertTrue(resultado.getHistoricoSaude().isEmpty());
        assertNotNull(resultado.getVacinacoes());
        assertTrue(resultado.getVacinacoes().isEmpty());
        verify(animalUseCase).updateAnimal(1L, dtoComListasVazias);
    }

    @Test
    void createAnimal_comCamposNulosOpcionais_deveAceitarERetornar() {
        AnimalDto dtoComNulos = new AnimalDto(
                null,
                "Miau",
                2,
                null, // raca null
                "RG-2024-002",
                "Gato",
                "F",
                null, // cor null
                null, // comportamento null
                null, // historico null
                LocalDate.of(2024, 1, 1),
                true,
                null,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
        when(animalUseCase.createAnimal(any(AnimalDto.class))).thenReturn(dtoComNulos);

        ResponseEntity<AnimalModel> response = animalController.createAnimal(dtoComNulos);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        AnimalDto resultado = response.getBody().getAnimalDto();
        assertNull(resultado.getRaca());
        assertNull(resultado.getCor());
        assertNull(resultado.getComportamento());
        assertNull(resultado.getHistorico());
        verify(animalUseCase).createAnimal(dtoComNulos);
    }

    @Test
    void createAnimal_comMultiplosHistoricosSaude_deveRetornarTodosOsRegistros() {
        List<HistoricoSaudeDto> multiplosHistoricos = new ArrayList<>();
        multiplosHistoricos.add(HistoricoSaudeDto.builder()
                .id(1L)
                .idAnimal(1L)
                .tipoEvento("Castração")
                .descricao("Animal castrado com sucesso")
                .castrado(true)
                .dataEvento(LocalDateTime.of(2023, 6, 10, 14, 0))
                .build());
        multiplosHistoricos.add(HistoricoSaudeDto.builder()
                .id(2L)
                .idAnimal(1L)
                .tipoEvento("Consulta")
                .descricao("Consulta pós-castração")
                .castrado(true)
                .dataEvento(LocalDateTime.of(2023, 6, 24, 10, 30))
                .build());

        AnimalDto dtoComMultiplosHistoricos = new AnimalDto(
                1L, "Rex", 3, "Labrador", "RG-2024-001", "Cachorro", "M", "Marrom",
                "Calmo", "Resgatado", LocalDate.of(2023, 4, 1), true,
                null, null, multiplosHistoricos, new ArrayList<>()
        );
        when(animalUseCase.createAnimal(any(AnimalDto.class))).thenReturn(dtoComMultiplosHistoricos);

        ResponseEntity<AnimalModel> response = animalController.createAnimal(dtoComMultiplosHistoricos);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        AnimalDto resultado = response.getBody().getAnimalDto();
        assertNotNull(resultado.getHistoricoSaude());
        assertEquals(2, resultado.getHistoricoSaude().size());
        assertEquals("Castração", resultado.getHistoricoSaude().get(0).getTipoEvento());
        assertEquals("Consulta", resultado.getHistoricoSaude().get(1).getTipoEvento());
        assertTrue(resultado.getHistoricoSaude().get(1).getCastrado());
        verify(animalUseCase).createAnimal(dtoComMultiplosHistoricos);
    }

    @Test
    void createAnimal_comMultiplasVacinacoes_deveRetornarTodasAsVacinas() {
        List<VacinacaoDto> multiplasVacinacoes = new ArrayList<>();
        multiplasVacinacoes.add(VacinacaoDto.builder()
                .id(1L)
                .idAnimal(1L)
                .nomeVacina("V10")
                .dataAplicacao(LocalDate.of(2024, 1, 15))
                .dose("1ª dose")
                .validade(LocalDate.of(2025, 1, 15))
                .veterinario("Dr. João Silva")
                .observacoes("Primeira aplicação")
                .build());
        multiplasVacinacoes.add(VacinacaoDto.builder()
                .id(2L)
                .idAnimal(1L)
                .nomeVacina("Antirrábica")
                .dataAplicacao(LocalDate.of(2024, 2, 10))
                .dose("Dose única")
                .validade(LocalDate.of(2025, 2, 10))
                .veterinario("Dr. João Silva")
                .observacoes("Vacinação obrigatória")
                .build());

        AnimalDto dtoComMultiplasVacinas = new AnimalDto(
                1L, "Rex", 3, "Labrador", "RG-2024-001", "Cachorro", "M", "Marrom",
                "Calmo", "Resgatado", LocalDate.of(2023, 4, 1), true,
                null, null, new ArrayList<>(), multiplasVacinacoes
        );
        when(animalUseCase.createAnimal(any(AnimalDto.class))).thenReturn(dtoComMultiplasVacinas);

        ResponseEntity<AnimalModel> response = animalController.createAnimal(dtoComMultiplasVacinas);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        AnimalDto resultado = response.getBody().getAnimalDto();
        assertNotNull(resultado.getVacinacoes());
        assertEquals(2, resultado.getVacinacoes().size());
        assertEquals("V10", resultado.getVacinacoes().get(0).getNomeVacina());
        assertEquals("Antirrábica", resultado.getVacinacoes().get(1).getNomeVacina());
        assertEquals("Dose única", resultado.getVacinacoes().get(1).getDose());
        verify(animalUseCase).createAnimal(dtoComMultiplasVacinas);
    }
}

