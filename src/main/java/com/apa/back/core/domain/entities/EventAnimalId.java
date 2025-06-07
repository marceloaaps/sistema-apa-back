package com.apa.back.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventAnimalId implements Serializable {

    @Column(name = "id_feirinha")
    private Long eventId;

    @Column(name = "id_animal")
    private Long animalId;

    public EventAnimalId() {}

    public EventAnimalId(Long animalId, Long eventId) {
        this.animalId = animalId;
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public EventAnimalId setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public EventAnimalId setAnimalId(Long animalId) {
        this.animalId = animalId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventAnimalId)) return false;
        EventAnimalId that = (EventAnimalId) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(animalId, that.animalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, animalId);
    }
}
