package com.apa.back.presentation.controllers.v1;

import com.apa.back.core.domain.entities.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events/v1")
public class EventController {

    @PostMapping
    public ResponseEntity<Event> createEvent() {

        Event event = createEvent();

    }

}
