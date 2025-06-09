package com.apa.back.core.domain.entities.Event;

import com.apa.back.core.domain.entities.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "feirinhas")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feirinha")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_responsavel")
    private User idResponsavel;

    @Column(name="data_inicio_feira")
    private Date startEventDate;

    @Column(name="data_fim_feira")
    private Date finishEventDate;

    @Column(name="localizacao")
    private String location;

    public User getIdResponsavel() {
        return idResponsavel;
    }

    public Event() {
    }

    public Event(Long id, Date finishEventDate, User idResponsavel, String location, Date startEventDate) {
        this.id = id;
        this.finishEventDate = finishEventDate;
        this.idResponsavel = idResponsavel;
        this.location = location;
        this.startEventDate = startEventDate;
    }

    public Event setIdResponsavel(User idResponsavel) {
        this.idResponsavel = idResponsavel;
        return this;
    }

    public Date getFinishEventDate() {
        return finishEventDate;
    }

    public Event setFinishEventDate(Date finishEventDate) {
        this.finishEventDate = finishEventDate;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Event setLocation(String location) {
        this.location = location;
        return this;
    }

    public Date getStartEventDate() {
        return startEventDate;
    }

    public Event setStartEventDate(Date startEventDate) {
        this.startEventDate = startEventDate;
        return this;
    }

}
