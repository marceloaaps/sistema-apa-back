package com.apa.back.core.domain.services;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.entities.Event.Event;
import com.apa.back.core.domain.entities.Event.EventAnimal;
import com.apa.back.core.domain.entities.Event.EventAnimalId;
import com.apa.back.core.domain.entities.Event.EventWorker;
import com.apa.back.core.domain.entities.Event.EventWorkerId;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.domain.repositories.Event.EventAnimalRepository;
import com.apa.back.core.domain.repositories.Event.EventWorkerRepository;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.core.exceptions.DomainNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EventAssociationService {

    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final EventAnimalRepository eventAnimalRepository;
    private final EventWorkerRepository eventWorkerRepository;

    public EventAssociationService(
            AnimalRepository animalRepository,
            UserRepository userRepository,
            EventAnimalRepository eventAnimalRepository,
            EventWorkerRepository eventWorkerRepository) {
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
        this.eventAnimalRepository = eventAnimalRepository;
        this.eventWorkerRepository = eventWorkerRepository;
    }

    public List<Long> associateAnimalsToEvent(Event event, List<Long> idsAnimais) {
        if (idsAnimais == null || idsAnimais.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> animaisAssociados = new ArrayList<>();
        for (Long idAnimal : idsAnimais) {
            Animal animal = animalRepository.findById(idAnimal)
                    .orElseThrow(() -> new DomainNotFoundException("Animal não encontrado: ID " + idAnimal));
            EventAnimal link = new EventAnimal(animal, event, new EventAnimalId(event.getId(), animal.getId()));
            eventAnimalRepository.save(link);
            animaisAssociados.add(animal.getId());
        }
        return animaisAssociados;
    }

    public List<Long> associateVolunteersToEvent(Event event, List<Long> idsVoluntarios) {
        if (idsVoluntarios == null || idsVoluntarios.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> voluntariosAssociados = new ArrayList<>();
        for (Long idVoluntario : idsVoluntarios) {
            User voluntario = userRepository.findById(idVoluntario)
                    .orElseThrow(() -> new DomainNotFoundException("Voluntário não encontrado: ID " + idVoluntario));
            EventWorker link = new EventWorker(event, new EventWorkerId(event.getId(), voluntario.getId()), voluntario);
            eventWorkerRepository.save(link);
            voluntariosAssociados.add(voluntario.getId());
        }
        return voluntariosAssociados;
    }

    public void removeAllAssociationsForEvent(Long eventId) {
        eventAnimalRepository.deleteByFeirinha_Id(eventId);
        eventWorkerRepository.deleteByFeirinha_Id(eventId);
    }
}
