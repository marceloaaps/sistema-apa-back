package com.apa.back.core.use_cases.event;

import com.apa.back.core.domain.entities.*;
import com.apa.back.core.domain.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventUseCase {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final EventAnimalRepository eventAnimalRepository;
    private final EventWorkerRepository eventWorkerRepository;


    public EventUseCase (
            EventRepository eventRepository,
            UserRepository userRepository,
            AnimalRepository animalRepository,
            EventAnimalRepository eventAnimalRepository,
            EventWorkerRepository eventWorkerRepository
) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.eventAnimalRepository = eventAnimalRepository;
        this.eventWorkerRepository = eventWorkerRepository;
    }

    @Transactional
    public Event createEvent(Long idResponsavel, String localizacao, Date dataInicio, Date dataFim, List<Long> idsVoluntarios, List<Long> idsAnimais) {
        User responsavel = userRepository.findById(idResponsavel)
                .orElseThrow(() -> new RuntimeException("Usuário responsável não encontrado"));

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

        for (Long idVoluntario : idsVoluntarios) {
            User worker = userRepository.findById(idVoluntario)
                    .orElseThrow(() -> new RuntimeException("Voluntário não encontrado" + idVoluntario));

            EventWorker workerEvent = new EventWorker(savedEvent, new EventWorkerId(savedEvent.getId(), worker.getId()), worker);

            eventWorkerRepository.save(workerEvent);

        }

        return savedEvent;
    }
}
