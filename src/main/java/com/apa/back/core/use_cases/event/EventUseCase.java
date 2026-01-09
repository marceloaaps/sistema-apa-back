package com.apa.back.core.use_cases.event;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.entities.Event.*;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.core.domain.repositories.event.EventAnimalRepository;
import com.apa.back.core.domain.repositories.event.EventRepository;
import com.apa.back.core.domain.repositories.event.EventWorkerRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.core.domain.services.EventAssociationService;
import com.apa.back.core.domain.services.EventDomainService;
import com.apa.back.core.domain.services.UserDomainService;
import com.apa.back.presentation.v1.dtos.event.EventDto;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EventUseCase {

    private static final Logger logger = LogManager.getLogger(EventUseCase.class);
    private final UserDomainService userDomainService;
    private final EventDomainService eventDomainService;
    private final EventAssociationService eventAssociationService;
    private final EventRepository eventRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final EventAnimalRepository eventAnimalRepository;
    private final EventWorkerRepository eventWorkerRepository;

    public EventUseCase(
            UserDomainService userDomainService,
            EventDomainService eventDomainService,
            EventAssociationService eventAssociationService,
            EventRepository eventRepository,
            AnimalRepository animalRepository,
            UserRepository userRepository,
            EventAnimalRepository eventAnimalRepository,
            EventWorkerRepository eventWorkerRepository) {

        this.userDomainService = userDomainService;
        this.eventDomainService = eventDomainService;
        this.eventAssociationService = eventAssociationService;
        this.eventRepository = eventRepository;
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
        this.eventAnimalRepository = eventAnimalRepository;
        this.eventWorkerRepository = eventWorkerRepository;
    }

    @Transactional
    public EventDto createEvent(EventDto requestDTO) {
        logger.info("Iniciando criação de evento - Localização: {}, Responsável ID: {}",
                requestDTO.localizacao(), requestDTO.idResponsavel());

        try {
            Event event = saveEvent(requestDTO);
            EventDto result = getEventDto(requestDTO, event);

            logger.info("Evento criado com sucesso - ID: {}, Localização: {}, Animais: {}, Voluntários: {}",
                    event.getId(), event.getLocation(),
                    result.idsAnimais().size(), result.idsVoluntarios().size());

            return result;

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao criar evento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar evento - Localização: {}", requestDTO.localizacao(), e);
            throw new RuntimeException("Erro ao criar evento", e);
        }
    }

    private Event saveEvent(EventDto requestDTO) {
        var user = userDomainService.getUserById(requestDTO.idResponsavel());
        return eventDomainService.createEvent(user, requestDTO.localizacao(), requestDTO.dataInicioFeira(), requestDTO.dataFimFeira());
    }

    public void deleteEvent(Long id) {
        logger.info("Iniciando exclusão do evento ID: {}", id);

        try {
            Event event = eventRepository.getEventById(id);
            if (event == null) {
                logger.warn("Evento não encontrado para exclusão - ID: {}", id);
                throw new DomainNotFoundException("Event not found with ID: " + id);
            }

            // remove associations first to avoid FK constraint issues
            removeAllAssociationsForEvent(event.getId());

            eventRepository.deleteById(id);
            logger.info("Evento deletado com sucesso - ID: {}", id);

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao deletar evento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar evento ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar evento", e);
        }
    }

    @Transactional
    public EventDto updateEvent(Long id, EventDto requestDTO) {
        logger.info("Iniciando atualização do evento ID: {}", id);

        try {
            Event event = eventRepository.getEventById(id);
            if (event == null) {
                logger.warn("Evento não encontrado para atualização - ID: {}", id);
                throw new DomainNotFoundException("Event not found with ID: " + id);
            }

            event.setLocation(requestDTO.localizacao());
            event.setStartEventDate(requestDTO.dataInicioFeira());
            event.setFinishEventDate(requestDTO.dataFimFeira());

            eventRepository.save(event);
            removeAllAssociationsForEvent(event.getId());

            EventDto result = getEventDto(requestDTO, event);
            logger.info("Evento atualizado com sucesso - ID: {}, Localização: {}", id, event.getLocation());

            return result;

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao atualizar evento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar evento ID: {}", id, e);
            throw new RuntimeException("Erro ao atualizar evento", e);
        }
    }

    private EventDto getEventDto(EventDto requestDTO, Event event) {
        List<Long> animaisAssociados = associateAnimalsToEvent(event, requestDTO.idsAnimais());
        List<Long> voluntariosAssociados = associateVolunteersToEvent(event, requestDTO.idsVoluntarios());
        Long idResponsavel = event.getIdResponsavel() != null ? event.getIdResponsavel().getId() : requestDTO.idResponsavel();

        return new EventDto(
                event.getId(),
                event.getStartEventDate(),
                event.getFinishEventDate(),
                event.getLocation(),
                voluntariosAssociados,
                animaisAssociados,
                idResponsavel
        );
    }

    private List<Long> associateAnimalsToEvent(Event event, List<Long> idsAnimais) {
        if (idsAnimais == null || idsAnimais.isEmpty()) {
            return Collections.emptyList();
        }

        logger.info("Associando {} animais ao evento ID: {}", idsAnimais.size(), event.getId());

        try {
            eventAssociationService.validateEventCanReceiveAssociations(event);

            List<Long> animaisAssociados = new ArrayList<>();
            for (Long idAnimal : idsAnimais) {
                Animal animal = animalRepository.findById(idAnimal)
                        .orElseThrow(() -> {
                            logger.warn("Animal não encontrado para associação - ID: {}", idAnimal);
                            return new DomainNotFoundException("Animal não encontrado: ID " + idAnimal);
                        });

                eventAssociationService.validateAnimalAssociation(animal, event);

                EventAnimal link = new EventAnimal(animal, event, new EventAnimalId(event.getId(), animal.getId()));
                eventAnimalRepository.save(link);
                animaisAssociados.add(animal.getId());
            }

            logger.info("Animais associados com sucesso ao evento ID: {}", event.getId());
            return animaisAssociados;

        } catch (DomainNotFoundException e) {
            logger.error("Erro ao associar animais ao evento ID {}: {}", event.getId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao associar animais ao evento ID: {}", event.getId(), e);
            throw new RuntimeException("Erro ao associar animais ao evento", e);
        }
    }

    private List<Long> associateVolunteersToEvent(Event event, List<Long> idsVoluntarios) {
        if (idsVoluntarios == null || idsVoluntarios.isEmpty()) {
            return Collections.emptyList();
        }

        eventAssociationService.validateEventCanReceiveAssociations(event);

        List<Long> voluntariosAssociados = new ArrayList<>();
        for (Long idVoluntario : idsVoluntarios) {
            User voluntario = userRepository.findById(idVoluntario)
                    .orElseThrow(() -> new DomainNotFoundException("Voluntário não encontrado: ID " + idVoluntario));

            eventAssociationService.validateVolunteerAssociation(voluntario, event);

            EventWorker link = new EventWorker(event, new EventWorkerId(event.getId(), voluntario.getId()), voluntario);
            eventWorkerRepository.save(link);
            voluntariosAssociados.add(voluntario.getId());
        }
        return voluntariosAssociados;
    }

    private void removeAllAssociationsForEvent(Long eventId) {
        eventAnimalRepository.deleteByFeirinha_Id(eventId);
        eventWorkerRepository.deleteByFeirinha_Id(eventId);
    }
}
