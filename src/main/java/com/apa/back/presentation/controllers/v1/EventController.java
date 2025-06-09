package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.use_cases.event.EventUseCase;
import com.apa.back.presentation.dtos.EventDto;
import com.apa.back.presentation.dtos.PaginacaoDto;
import com.apa.back.presentation.dtos.ReturnEventDto;
import com.apa.back.presentation.dtos.UsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.websocket.server.PathParam;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events/v1")
public class EventController {

    private final EventUseCase eventUseCase;

    public EventController(EventUseCase eventUseCase) {
        this.eventUseCase = eventUseCase;
    }

    @Operation(
            summary = "Criação de um evento (feirinha)",
            description = "Cria um novo evento (feirinha) com animais e voluntários associados. Envie um JSON com ID do responsável, localização, datas, IDs dos voluntários e IDs dos animais.",
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
                            description = "Usuário responsável, voluntário ou animal não encontrado"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventRequestDTO) {
        EventDto eventResponse = eventUseCase.createEvent(eventRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventResponse);
    }

    @Operation(
            summary = "Listar eventos (feirinhas) com paginação",
            description = "Retorna uma lista paginada de eventos (feirinhas), incluindo dados como local, datas, voluntários e animais.",
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
    public ResponseEntity<PaginacaoDto<EventDto>> getAllEvents(
            @Parameter(description = "Parâmetros de paginação")
            @PageableDefault(size = 20, sort = "startEventDate") Pageable pageable) {
        Page<EventDto> page = eventUseCase.getAllEvents(pageable);
        return ResponseEntity.ok(new PaginacaoDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        ));
    }

    public ResponseEntity<String> deleteEvent(Long id) {

        eventUseCase.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<ReturnEventDto> getEventById(@PathVariable Long id) {
        ReturnEventDto eventFound = eventUseCase.getEventById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventFound);
    }


}


