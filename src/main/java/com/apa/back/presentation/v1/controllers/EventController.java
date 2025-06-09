package com.apa.back.presentation.v1.controllers;

import com.apa.back.core.use_cases.event.EventUseCase;
import com.apa.back.core.use_cases.event.GetEventsUseCase;
import com.apa.back.presentation.dtos.event.EventDto;
import com.apa.back.presentation.dtos.event.EventModel;
import com.apa.back.presentation.dtos.event.ReturnEventModel;
import com.apa.back.presentation.dtos.utils.PaginacaoDto;
import com.apa.back.presentation.dtos.event.ReturnEventDto;
import com.apa.back.presentation.v1.assemblers.EventModelAssembler;
import com.apa.back.presentation.v1.assemblers.ReturnEventModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Eventos (Feirinhas)", description = "API para gerenciamento de eventos (feirinhas).")
@RestController
@RequestMapping("/events/v1")
public class EventController {

    private final EventUseCase eventUseCase;
    private final GetEventsUseCase getEventsUseCase;

    public EventController(EventUseCase eventUseCase, GetEventsUseCase getEventsUseCase) {
        this.eventUseCase = eventUseCase;
        this.getEventsUseCase = getEventsUseCase;
    }

    @Operation(
            summary = "Criação de um evento",
            description = "Cria uma nova feirinha com animais e voluntários associados. Envie um JSON com ID do responsável, localização, datas, IDs dos voluntários e IDs dos animais.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Evento criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário responsável, voluntário ou animal não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventRequestDTO) {
        EventDto eventResponse = eventUseCase.createEvent(eventRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventResponse);
    }

    @Operation(
            summary = "Listar eventos com paginação",
            description = "Retorna uma lista paginada de feirinhas, incluindo dados como local, datas, voluntários e animais.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada de eventos retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaginacaoDto.class)
                            )
                    )
            }
    )

    @GetMapping
    public ResponseEntity<PagedModel<EventModel>> getAllEvents(
            @Parameter(description = "Parâmetros de paginação")
            @PageableDefault(size = 20, sort = "startEventDate") Pageable pageable,
            PagedResourcesAssembler<EventDto> pagedResourcesAssembler) {

        Page<EventDto> page = getEventsUseCase.getAllEvents(pageable);

        PagedModel<EventModel> pagedModel = pagedResourcesAssembler.toModel(
                page,
                EventModelAssembler::toModel
        );

        pagedModel.add(linkTo(methodOn(EventController.class).createEvent(null)).withRel("create"));

        return ResponseEntity.ok(pagedModel);
    }
    @Operation(
            summary = "Deletar evento (feirinha) pelo ID",
            description = "Remove o evento identificado pelo ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Evento deletado com sucesso",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evento não encontrado",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(
            @Parameter(description = "ID do evento a ser deletado", required = true)
            @PathVariable Long id) {

        eventUseCase.deleteEvent(id);

        return ResponseEntity.ok("Evento deletado com sucesso.");
    }

    @Operation(
            summary = "Buscar evento (feirinha) pelo ID",
            description = "Retorna os dados completos do evento identificado pelo ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Evento encontrado e retornado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReturnEventDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evento não encontrado",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReturnEventModel> getEventById(
            @Parameter(description = "ID do evento a ser buscado", required = true)
            @PathVariable Long id) {

        ReturnEventDto eventFound = getEventsUseCase.getEventById(id);

        ReturnEventModel model = ReturnEventModelAssembler.toModel(eventFound);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(model);
    }

    @Operation(
            summary = "Atualizar evento (feirinha) pelo ID",
            description = "Atualiza os dados do evento identificado pelo ID. Envie o JSON com as informações a serem atualizadas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Evento atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EventDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evento não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EventModel> updateEvent(
            @Parameter(description = "ID do evento a ser atualizado", required = true)
            @PathVariable Long id,
            @RequestBody EventDto eventRequestDTO) {

        EventDto updatedDto = eventUseCase.updateEvent(id, eventRequestDTO);

        EventModel model = EventModelAssembler.toModel(updatedDto);

        return ResponseEntity.ok(model);
    }


}
