package com.apa.back;

import com.apa.back.core.use_cases.event.EventUseCase;
import com.apa.back.core.use_cases.event.GetEventsUseCase;
import com.apa.back.presentation.v1.controllers.EventController;
import com.apa.back.presentation.v1.dtos.animal.AnimalDto;
import com.apa.back.presentation.v1.dtos.event.EventDto;
import com.apa.back.presentation.v1.dtos.event.ReturnEventDto;
import com.apa.back.presentation.v1.dtos.user.UsuarioWithIdDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventController.class)
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventUseCase eventUseCase;

    @MockBean
    private GetEventsUseCase getEventsUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEventSuccessfully() throws Exception {
        EventDto inputDto = new EventDto(
                null,
                new Date(2025 - 1900, 5, 10, 8, 0, 0),
                new Date(2025 - 1900, 5, 10, 17, 0, 0),
                "Praça Nova",
                Arrays.asList(1L, 2L),
                Arrays.asList(10L, 20L),
                1L
        );

        EventDto returnedDto = new EventDto(
                1L,
                inputDto.dataInicioFeira(),
                inputDto.dataFimFeira(),
                inputDto.localizacao(),
                inputDto.idsVoluntarios(),
                inputDto.idsAnimais(),
                1L
        );

        when(eventUseCase.createEvent(any(EventDto.class))).thenReturn(returnedDto);

        mockMvc.perform(post("/events/v1")
                        .with(csrf())
                        .with(user("testuser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idFeirinha").value(1))
                .andExpect(jsonPath("$.localizacao").value("Praça Nova"))
                .andExpect(jsonPath("$.idsVoluntarios[0]").value(1))
                .andExpect(jsonPath("$.idsAnimais[1]").value(20));
    }


    @Test
    void shouldGetEventByIdSuccessfully() throws Exception {
        Long eventId = 1L;

        List<UsuarioWithIdDto> voluntarios = Arrays.asList(
                new UsuarioWithIdDto(1L, "Voluntário 1", "voluntario1@example.com", LocalDate.of(1990, 1, 1)),
                new UsuarioWithIdDto(2L, "Voluntário 2", "voluntario2@example.com", LocalDate.of(1992, 2, 2))
        );

        List<AnimalDto> animais = Arrays.asList(
                new AnimalDto(10L, "Rex", 3, "Labrador", "RG-001", "Cachorro", "M", "Marrom", "Calmo e amigável", "Encontrado na rua, saudável", LocalDate.of(2023, 4, 1), true),
                new AnimalDto(20L, "Mimi", 2, "Poodle", "RG-002", "Cachorro", "F", "Branco", "Brincalhona", "Resgatada de um abrigo", LocalDate.of(2023, 5, 10), true)
        );

        ReturnEventDto returnEventDto = new ReturnEventDto(
                1L,
                5L,
                new Date(2025 - 1900, 5, 10, 11, 0, 0),
                new Date(2025 - 1900, 5, 10, 20, 0, 0),
                "Praça Local",
                voluntarios,
                animais
        );

        when(getEventsUseCase.getEventById(eq(eventId))).thenReturn(returnEventDto);

        mockMvc.perform(get("/events/v1/{id}", eventId)
                        .with(csrf())
                        .with(user("testuser").roles("USER")))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.dto.idFeirinha").value(5))
                .andExpect(jsonPath("$.dto.localizacao").value("Praça Local"))
                .andExpect(jsonPath("$.dto.usuarios[0].id").value(1))
                .andExpect(jsonPath("$.dto.animais[1].id").value(20));
    }



    @Test
    void shouldDeleteEventSuccessfully() throws Exception {
        Long eventId = 1L;

        // Não precisa mockar retorno de void

        mockMvc.perform(delete("/events/v1/{id}", eventId)
                        .with(csrf())
                        .with(user("testuser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string("Evento deletado com sucesso."));
    }

    @Test
    void shouldGetAllEventsSuccessfully() throws Exception {
        // Mockando uma página com EventDto para o getAllEventsUseCase
        EventDto event1 = new EventDto(
                1L,
                new Date(2025 - 1900, 5, 10, 8, 0, 0),
                new Date(2025 - 1900, 5, 10, 17, 0, 0),
                "Local 1",
                Arrays.asList(1L),
                Arrays.asList(10L),
                1L
        );

        List<EventDto> eventList = List.of(event1);
        Page<EventDto> page = new PageImpl<>(eventList, PageRequest.of(0, 20), eventList.size());

        when(getEventsUseCase.getAllEvents(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/events/v1")
                        .with(csrf())
                        .with(user("testuser").roles("USER"))
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.eventModelList[0].dto.idFeirinha").value(1))
                .andExpect(jsonPath("$._embedded.eventModelList[0].dto.localizacao").value("Local 1"))
                .andExpect(jsonPath("$._embedded.eventModelList[0].dto.idsVoluntarios[0]").value(1))
                .andExpect(jsonPath("$._embedded.eventModelList[0].dto.idsAnimais[0]").value(10))
                .andExpect(jsonPath("$._links.create.href").exists());
    }

    @Test
    void shouldUpdateEventSuccessfully() throws Exception {
        EventDto inputDto = new EventDto(
                1L,
                new Date(2025 - 1900, 5, 10, 8, 0, 0),
                new Date(2025 - 1900, 5, 10, 17, 0, 0),
                "Praça Atualizada",
                Arrays.asList(1L, 2L),
                Arrays.asList(10L, 20L),
                1L
        );

        EventDto returnedDto = inputDto;

        when(eventUseCase.updateEvent(eq(1L), any(EventDto.class))).thenReturn(returnedDto);

        mockMvc.perform(put("/events/v1/1")
                        .with(csrf())
                        .with(user("testuser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dto.idFeirinha").value(1))
                .andExpect(jsonPath("$.dto.localizacao").value("Praça Atualizada"))
                .andExpect(jsonPath("$.dto.idsVoluntarios[0]").value(1))
                .andExpect(jsonPath("$.dto.idsAnimais[1]").value(20));
    }
}
