package com.apa.back.core.domain.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class EventWorkerId implements Serializable {

    private Long eventId;
    private Long workerId;

    public EventWorkerId() {
    }

    public EventWorkerId(Long eventId, Long workerId) {
        this.eventId = eventId;
        this.workerId = workerId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public EventWorkerId setWorkerId(Long workerId) {
        this.workerId = workerId;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public EventWorkerId setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }
}
