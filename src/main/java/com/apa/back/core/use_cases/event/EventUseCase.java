package com.apa.back.core.use_cases.event;

import com.apa.back.core.domain.entities.*;
import com.apa.back.core.domain.repositories.AnimalRepository;
import com.apa.back.core.domain.repositories.EventAnimalRepository;
import com.apa.back.core.domain.repositories.EventRepository;
import com.apa.back.core.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventUseCase {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final EventAnimalRepository eventAnimalRepository;


    public EventUseCase (
            EventRepository eventRepository,
            UserRepository userRepository,
            AnimalRepository animalRepository,
            EventAnimalRepository eventAnimalRepository
    ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.eventAnimalRepository = eventAnimalRepository;
    }

    @Transactional
    public Event createEvent(Long idResponsavel, String localizacao, Date dataInicio, Date dataFim, List<Long> idsVoluntarios, List<Long> idsAnimais) {
        User responsavel = userRepository.findById(idResponsavel)
                .orElseThrow(() -> new RuntimeException("Usuário responsável não encontrado"));

        List<User> voluntarios = userRepository.findAllById(idsVoluntarios);

        Event event = new Event();
        event.setIdResponsavel(responsavel);
        event.setLocation(localizacao);
        event.setStartEventDate(dataInicio);
        event.setFinishEventDate(dataFim);

        Event savedEvent = eventRepository.save(event);

        for (Long idAnimal : idsAnimais) {
            Animal animal = animalRepository.findById(idAnimal)
                    .orElseThrow(() -> new RuntimeException("Animal não encontrado: ID " + idAnimal));
            EventAnimal link = new EventAnimal(animal, savedEvent, new EventAnimalId(savedEvent.getId(), animal.getId()));
            eventAnimalRepository.save(link);
        }

        return savedEvent;
    }
}
