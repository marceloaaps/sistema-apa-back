package com.apa.back.core.domain.entities.Event;

import com.apa.back.core.domain.entities.Animal;
import jakarta.persistence.*;

@Entity
@Table(name = "feirinhas_animais")
public class EventAnimal {

    @EmbeddedId
    private EventAnimalId id;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "id_feirinha")
    private Event feirinha;

    @ManyToOne
    @MapsId("animalId")
    @JoinColumn(name = "id_animal")
    private Animal animal;

    public EventAnimal() {
    }

    public EventAnimal(Animal animal, Event feirinha, EventAnimalId id) {
        this.animal = animal;
        this.feirinha = feirinha;
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public EventAnimal setAnimal(Animal animal) {
        this.animal = animal;
        return this;
    }

    public Event getFeirinha() {
        return feirinha;
    }

    public EventAnimal setFeirinha(Event feirinha) {
        this.feirinha = feirinha;
        return this;
    }

    public EventAnimalId getId() {
        return id;
    }

    public EventAnimal setId(EventAnimalId id) {
        this.id = id;
        return this;
    }

    // Getters e setters
}

