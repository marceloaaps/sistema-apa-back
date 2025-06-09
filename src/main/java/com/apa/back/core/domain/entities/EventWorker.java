package com.apa.back.core.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "feirinhas_voluntarios")
public class EventWorker {

    @EmbeddedId
    private EventWorkerId id;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "id_feirinha")
    private Event feirinha;

    @ManyToOne
    @MapsId("workerId")
    @JoinColumn(name = "id_voluntario")
    private User worker;

    public EventWorker() {
    }

    public EventWorker(Event feirinha, EventWorkerId id, User worker) {
        this.feirinha = feirinha;
        this.id = id;
        this.worker = worker;
    }

    public Event getFeirinha() {
        return feirinha;
    }

    public EventWorker setFeirinha(Event feirinha) {
        this.feirinha = feirinha;
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
        return worker;
    }

    public EventWorker setWorker(User worker) {
        this.worker = worker;
        return this;
    }
}

