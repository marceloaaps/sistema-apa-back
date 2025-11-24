package com.apa.back.core.services;

import com.apa.back.core.domain.entities.Event.Event;
import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EventDomainService {

    private final EventRepository eventRepository;

    public EventDomainService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(User responsavel, String localizacao, Date dataInicio, Date dataFim) {

        Event event = new Event();
        event.setIdResponsavel(responsavel);
        event.setLocation(localizacao);
        event.setStartEventDate(dataInicio);
        event.setFinishEventDate(dataFim);

        return eventRepository.save(event);
    }
}
