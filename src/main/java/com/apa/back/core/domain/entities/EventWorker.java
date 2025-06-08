package com.apa.back.core.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "feirinhas_voluntarios")
public class EventWorker {

    @EmbeddedId
    private EventWorkerId id;

    @ManyToOne
    @JoinColumn(name = "id_feirinha")
    @MapsId("eventId")
    private Event idEvent;

    @ManyToOne
    @JoinColumn(name = "id_voluntario")
    @MapsId("workerId")
    private User idWorker;

    public EventWorker() {
    }

    public EventWorker(Event idEvent, EventWorkerId id, User idWorker) {
        this.idEvent = idEvent;
        this.id = id;
        this.idWorker = idWorker;
    }




    public Event getIdEvent() {
        return idEvent;
    }

    public EventWorker setIdEvent(Event idEvent) {
        this.idEvent = idEvent;
        return this;
    }

    public EventWorkerId getId() {
        return id;
    }

    public EventWorker setId(EventWorkerId id) {
        this.id = id;
        return this;
    }

    public User getIdWorker() {
        return idWorker;
    }

    public EventWorker setIdWorker(User idWorker) {
        this.idWorker = idWorker;
        return this;
    }
}

