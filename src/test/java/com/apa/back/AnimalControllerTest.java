package com.apa.back;

import com.apa.back.core.use_cases.animal.AnimalUseCase;
import com.apa.back.presentation.v1.controllers.AnimalController;
import com.apa.back.presentation.v1.dtos.animal.AnimalDto;
import com.apa.back.presentation.v1.dtos.animal.AnimalModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;

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
                456L,
                "Calmo e amigável",
                "Encontrado na rua, saudável",
                LocalDate.of(2023, 4, 1),
                true
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
        verify(animalUseCase, times(1)).createAnimal(dtoEntrada);
    }

    @Test
    void updateAnimal_deveRetornarOkQuandoAnimalExistir() {
        AnimalDto dtoEntrada = criarAnimalDtoExemplo();
        when(animalUseCase.updateAnimal(eq(1L), any(AnimalDto.class))).thenReturn(dtoEntrada);

        ResponseEntity<AnimalModel> response = animalController.updateAnimal(1L, dtoEntrada);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(animalUseCase).updateAnimal(1L, dtoEntrada);
    }

    @Test
    void updateAnimal_deveRetornarNotFoundQuandoAnimalNaoExistir() {
        when(animalUseCase.updateAnimal(eq(999L), any(AnimalDto.class))).thenReturn(null);

        ResponseEntity<AnimalModel> response = animalController.updateAnimal(999L, criarAnimalDtoExemplo());

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(animalUseCase).updateAnimal(eq(999L), any());
    }

    @Test
    void deleteAnimal_deveRetornarNoContentQuandoExcluirComSucesso() {
        when(animalUseCase.deleteAnimal(1L)).thenReturn(true);

        ResponseEntity<Void> response = animalController.deleteAnimal(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(animalUseCase).deleteAnimal(1L);
    }

    @Test
    void deleteAnimal_deveRetornarNotFoundQuandoNaoExcluir() {
        when(animalUseCase.deleteAnimal(999L)).thenReturn(false);

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
        verify(animalUseCase).restoreAnimal(1L);
    }

    @Test
    void restoreAnimal_deveRetornarNotFoundQuandoNaoRestaurar() {
        when(animalUseCase.restoreAnimal(999L)).thenReturn(null);

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
        verify(animalUseCase).getAnimalById(1L);
    }

    @Test
    void getAnimalById_deveRetornarNotFoundQuandoNaoEncontrar() {
        when(animalUseCase.getAnimalById(999L)).thenReturn(null);

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

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
        PagedModel<AnimalModel> pagedModel = PagedModel.of(Collections.emptyList(), metadata);

        when(animalUseCase.getAnimaisDisponiveis(pageable)).thenReturn(page);

        when(pagedResourcesAssembler.toModel(
                eq(page),
                ArgumentMatchers.<RepresentationModelAssembler<AnimalDto, AnimalModel>>any())
        ).thenReturn(pagedModel);

        ResponseEntity<PagedModel<AnimalModel>> response = animalController.getAnimaisDisponiveis(pageable, pagedResourcesAssembler);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(animalUseCase).getAnimaisDisponiveis(pageable);

        verify(pagedResourcesAssembler).toModel(
                eq(page),
                ArgumentMatchers.<RepresentationModelAssembler<AnimalDto, AnimalModel>>any()
        );
    }

}
