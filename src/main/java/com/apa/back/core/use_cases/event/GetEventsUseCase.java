package com.apa.back.core.use_cases.event;

import com.apa.back.core.domain.entities.Event.Event;
import com.apa.back.core.domain.entities.Event.EventAnimal;
import com.apa.back.core.domain.entities.Event.EventWorker;
import com.apa.back.core.domain.repositories.event.EventAnimalRepository;
import com.apa.back.core.domain.repositories.event.EventRepository;
import com.apa.back.core.domain.repositories.event.EventWorkerRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.infra.mappers.DtoMapper;
import com.apa.back.presentation.v1.dtos.animal.AnimalDto;
import com.apa.back.presentation.v1.dtos.event.EventDto;
import com.apa.back.presentation.v1.dtos.event.ReturnEventDto;
import com.apa.back.presentation.v1.dtos.user.UsuarioWithIdDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetEventsUseCase {

    private static final Logger logger = LogManager.getLogger(GetEventsUseCase.class);

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
        logger.info("Buscando evento por ID: {}", id);

        try {
            Event event = eventRepository.getEventById(id);
            if (event == null) {
                logger.warn("Evento não encontrado - ID: {}", id);
                throw new DomainNotFoundException("Event not found with ID: " + id);
            }

            List<EventAnimal> animaisEvento = eventAnimalRepository.findByFeirinha(event);
            List<EventWorker> voluntariosEvento = eventWorkerRepository.findByFeirinha(event);

            List<AnimalDto> animalDtos = dtoMapper.mapToAnimalDtos(animaisEvento);
            List<UsuarioWithIdDto> usuarioDtos = dtoMapper.mapToUsuarioDtos(voluntariosEvento);

            logger.info("Evento encontrado - ID: {}, Localização: {}, Animais: {}, Voluntários: {}",
                    id, event.getLocation(), animalDtos.size(), usuarioDtos.size());

            return new ReturnEventDto(
                    event.getIdResponsavel().getId(),
                    event.getId(),
                    event.getStartEventDate(),
                    event.getFinishEventDate(),
                    event.getLocation(),
                    usuarioDtos,
                    animalDtos
            );

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao buscar evento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar evento ID: {}", id, e);
            throw new RuntimeException("Erro ao buscar evento", e);
        }
    }

    public Page<EventDto> getAllEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAll(pageable);

        List<EventDto> eventDtos = eventPage.stream().map(event -> {
            List<Long> idsAnimais = eventAnimalRepository.findByFeirinha(event).stream()
                    .map(ea -> ea.getAnimal().getId())
                    .toList();

            List<Long> idsVoluntarios = eventWorkerRepository.findByFeirinha(event).stream()
                    .map(ew -> ew.getIdWorker().getId())
                    .toList();

            return new EventDto(
                    event.getId(),
                    event.getStartEventDate(),
                    event.getFinishEventDate(),
                    event.getLocation(),
                    idsAnimais,
                    idsVoluntarios,
                    event.getIdResponsavel().getId()
            );
        }).toList();


        return new PageImpl<>(eventDtos, pageable, eventPage.getTotalElements());
    }



}
