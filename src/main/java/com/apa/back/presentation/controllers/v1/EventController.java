package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.use_cases.event.EventUseCase;
import com.apa.back.presentation.dtos.EventDto;
import com.apa.back.presentation.dtos.UsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
