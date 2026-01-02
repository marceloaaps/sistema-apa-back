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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EventUseCase {
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
        Event event = saveEvent(requestDTO);
        return getEventDto(requestDTO, event);
    }

    private Event saveEvent(EventDto requestDTO) {
        var user = userDomainService.getUserById(requestDTO.idResponsavel());
        return eventDomainService.createEvent(user, requestDTO.localizacao(), requestDTO.dataInicioFeira(), requestDTO.dataFimFeira());
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.getEventById(id);
        if (event == null) {
            throw new DomainNotFoundException("Event not found with ID: " + id);
        }

        // remove associations first to avoid FK constraint issues
        removeAllAssociationsForEvent(event.getId());

        eventRepository.deleteById(id);
    }

    @Transactional
    public EventDto updateEvent(Long id, EventDto requestDTO) {
        Event event = eventRepository.getEventById(id);
        if (event == null) {
            throw new DomainNotFoundException("Event not found with ID: " + id);
        }
        event.setLocation(requestDTO.localizacao());
        event.setStartEventDate(requestDTO.dataInicioFeira());
        event.setFinishEventDate(requestDTO.dataFimFeira());

        eventRepository.save(event);
        removeAllAssociationsForEvent(event.getId());
        return getEventDto(requestDTO, event);
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

        eventAssociationService.validateEventCanReceiveAssociations(event);

        List<Long> animaisAssociados = new ArrayList<>();
        for (Long idAnimal : idsAnimais) {
            Animal animal = animalRepository.findById(idAnimal)
                    .orElseThrow(() -> new DomainNotFoundException("Animal não encontrado: ID " + idAnimal));

            eventAssociationService.validateAnimalAssociation(animal, event);

            EventAnimal link = new EventAnimal(animal, event, new EventAnimalId(event.getId(), animal.getId()));
            eventAnimalRepository.save(link);
            animaisAssociados.add(animal.getId());
        }
        return animaisAssociados;
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
