package com.apa.back.core.use_cases.event;

import com.apa.back.core.domain.entities.*;
import com.apa.back.core.domain.entities.Event.*;
import com.apa.back.core.domain.repositories.*;
import com.apa.back.presentation.v1.dtos.event.EventDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public EventDto createEvent(EventDto requestDTO) {


        Event event = createAndSaveEvent(requestDTO.idFeirinha(), requestDTO.localizacao(), requestDTO.dataInicioFeira(), requestDTO.dataFimFeira());
        List<Long> animaisAssociados = associateAnimalsToEvent(event, requestDTO.idsAnimais());
        List<Long> voluntariosAssociados = associateVolunteersToEvent(event, requestDTO.idsVoluntarios());
        return new EventDto(event.getId(), event.getStartEventDate(), event.getFinishEventDate(), event.getLocation(), animaisAssociados, voluntariosAssociados); // Assumindo que esse DTO tem construtor a partir de Event
    }

    private Event createAndSaveEvent(Long idResponsavel, String localizacao, Date dataInicio, Date dataFim) {
        User responsavel = userRepository.findById(idResponsavel)
                .orElseThrow(() -> new RuntimeException("Usuário responsável não encontrado"));

        Event event = new Event();
        event.setIdResponsavel(responsavel);
        event.setLocation(localizacao);
        event.setStartEventDate(dataInicio);
        event.setFinishEventDate(dataFim);

        return eventRepository.save(event);
    }

    private List<Long> associateAnimalsToEvent(Event event, List<Long> idsAnimais) {
        List<Long> animaisAssociados = new ArrayList<>();
        for (Long idAnimal : idsAnimais) {
            Animal animal = animalRepository.findById(idAnimal)
                    .orElseThrow(() -> new RuntimeException("Animal não encontrado: ID " + idAnimal));
            EventAnimal link = new EventAnimal(animal, event, new EventAnimalId(event.getId(), animal.getId()));
            eventAnimalRepository.save(link);
            animaisAssociados.add(animal.getId());
        }
        return animaisAssociados;
    }

    private List<Long> associateVolunteersToEvent(Event event, List<Long> idsVoluntarios) {
        List<Long> voluntariosAssociados = new ArrayList<>();
        for (Long idVoluntario : idsVoluntarios) {
            User voluntario = userRepository.findById(idVoluntario)
                    .orElseThrow(() -> new RuntimeException("Voluntário não encontrado: ID " + idVoluntario));
            EventWorker link = new EventWorker(event, new EventWorkerId(event.getId(), voluntario.getId()), voluntario);
            eventWorkerRepository.save(link);
            voluntariosAssociados.add(voluntario.getId());
        }
        return voluntariosAssociados;
    }



    public void deleteEvent(Long id) {
        Event event = eventRepository.getEventById(id);

        eventRepository.deleteById(id);

        eventAnimalRepository.deleteByFeirinha_Id(event.getId());
        eventWorkerRepository.deleteByFeirinha_Id(event.getId());
    }

    public EventDto updateEvent(Long id, EventDto requestDTO) {
        Event event = eventRepository.getEventById(id);
        event.setLocation(requestDTO.localizacao());
        event.setStartEventDate(requestDTO.dataInicioFeira());
        event.setFinishEventDate(requestDTO.dataFimFeira());

        eventRepository.save(event);
        eventAnimalRepository.deleteByFeirinha_Id(event.getId());
        eventWorkerRepository.deleteByFeirinha_Id(event.getId());
        List<Long> animaisAssociados = associateAnimalsToEvent(event, requestDTO.idsAnimais());
        List<Long> voluntariosAssociados = associateVolunteersToEvent(event, requestDTO.idsVoluntarios());

        return new EventDto(
                event.getId(),
                event.getStartEventDate(),
                event.getFinishEventDate(),
                event.getLocation(),
                animaisAssociados,
                voluntariosAssociados
        );
    }


}
