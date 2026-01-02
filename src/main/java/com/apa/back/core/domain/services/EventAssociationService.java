package com.apa.back.core.domain.services;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.entities.Event.Event;
import com.apa.back.core.exceptions.DomainException;
import org.springframework.stereotype.Service;

@Service
public class EventAssociationService {
    public void validateAnimalAssociation(Animal animal, Event event) {
        if (animal == null) {
            throw new DomainException("Animal não pode ser nulo");
        }

        if (event == null) {
            throw new DomainException("Evento não pode ser nulo");
        }

        if (animal.getDeletadoEm() != null) {
            throw new DomainException("Animal deletado não pode ser associado a eventos");
        }

        // Adicionar algo para se o animal não estiver disponível para adoção
        if (animal.getDisponivelParaAdocao() != null && !animal.getDisponivelParaAdocao()) {

        }
    }

    public void validateVolunteerAssociation(User volunteer, Event event) {
        if (volunteer == null) {
            throw new DomainException("Voluntário não pode ser nulo");
        }

        if (event == null) {
            throw new DomainException("Evento não pode ser nulo");
        }

    }

    public void validateEventCanReceiveAssociations(Event event) {
        if (event == null) {
            throw new DomainException("Evento não pode ser nulo");
        }
    }
}
