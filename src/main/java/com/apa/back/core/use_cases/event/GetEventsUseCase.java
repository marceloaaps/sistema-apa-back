package com.apa.back.core.use_cases.event;

import com.apa.back.core.domain.entities.*;
import com.apa.back.core.domain.repositories.EventAnimalRepository;
import com.apa.back.core.domain.repositories.EventRepository;
import com.apa.back.core.domain.repositories.EventWorkerRepository;
import com.apa.back.infra.exceptions.ResourceNotFoundException;
import com.apa.back.infra.mappers.DtoMapper;
import com.apa.back.presentation.dtos.animal.AnimalDto;
import com.apa.back.presentation.dtos.event.EventDto;
import com.apa.back.presentation.dtos.event.ReturnEventDto;
import com.apa.back.presentation.dtos.user.UsuarioWithIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetEventsUseCase {

    private final EventRepository eventRepository;
    private final EventAnimalRepository eventAnimalRepository;
    private final EventWorkerRepository eventWorkerRepository;
    private final DtoMapper dtoMapper;

    public GetEventsUseCase(EventRepository eventRepository, EventAnimalRepository eventAnimalRepository, EventWorkerRepository eventWorkerRepository, DtoMapper dtoMapper) {
        this.eventRepository = eventRepository;
        this.eventAnimalRepository = eventAnimalRepository;
        this.eventWorkerRepository = eventWorkerRepository;
        this.dtoMapper = dtoMapper;
    }
    public ReturnEventDto getEventById(Long id) {
        Event event = eventRepository.getEventById(id);
        if (event == null) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }

        List<EventAnimal> animaisEvento = eventAnimalRepository.findByFeirinha(event);
        List<EventWorker> voluntariosEvento = eventWorkerRepository.findByFeirinha(event);

        List<AnimalDto> animalDtos = dtoMapper.mapToAnimalDtos(animaisEvento);
        List<UsuarioWithIdDto> usuarioDtos = dtoMapper.mapToUsuarioDtos(voluntariosEvento);

        return new ReturnEventDto(
                event.getIdResponsavel().getId(),
                event.getId(),
                event.getStartEventDate(),
                event.getFinishEventDate(),
                event.getLocation(),
                usuarioDtos,
                animalDtos
        );
    }

    public Page<EventDto> getAllEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAll(pageable);

        List<EventDto> eventDtos = eventPage.stream().map(event -> {
            List<Long> idsAnimais = eventAnimalRepository.findByFeirinha(event).stream()
                    .map(ea -> ea.getAnimal().getId())
                    .collect(Collectors.toList());

            List<Long> idsVoluntarios = eventWorkerRepository.findByFeirinha(event).stream()
                    .map(ew -> ew.getIdWorker().getId())
                    .collect(Collectors.toList());

            return new EventDto(
                    event.getId(),
                    event.getStartEventDate(),
                    event.getFinishEventDate(),
                    event.getLocation(),
                    idsAnimais,
                    idsVoluntarios
            );
        }).collect(Collectors.toList());


        return new PageImpl<>(eventDtos, pageable, eventPage.getTotalElements());
    }



}
