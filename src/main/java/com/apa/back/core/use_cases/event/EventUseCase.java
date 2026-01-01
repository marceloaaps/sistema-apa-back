package com.apa.back.core.use_cases.event;

import com.apa.back.core.domain.entities.Event.*;
import com.apa.back.core.domain.repositories.event.EventRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.core.domain.services.EventAssociationService;
import com.apa.back.core.domain.services.EventDomainService;
import com.apa.back.core.domain.services.UserDomainService;
import com.apa.back.presentation.v1.dtos.event.EventDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventUseCase {
    private final UserDomainService userDomainService;
    private final EventDomainService eventDomainService;
    private final EventRepository eventRepository;
    private final EventAssociationService eventAssociationService;


    public EventUseCase (
            UserDomainService userDomainService,
            EventDomainService eventDomainService,
            EventRepository eventRepository,
            EventAssociationService eventAssociationService) {

        this.userDomainService = userDomainService;
        this.eventDomainService = eventDomainService;
        this.eventRepository = eventRepository;
        this.eventAssociationService = eventAssociationService;
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
        eventAssociationService.removeAllAssociationsForEvent(event.getId());

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
        eventAssociationService.removeAllAssociationsForEvent(event.getId());
        return getEventDto(requestDTO, event);
    }

    private EventDto getEventDto(EventDto requestDTO, Event event) {
        List<Long> animaisAssociados = eventAssociationService.associateAnimalsToEvent(event, requestDTO.idsAnimais());
        List<Long> voluntariosAssociados = eventAssociationService.associateVolunteersToEvent(event, requestDTO.idsVoluntarios());
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


}
