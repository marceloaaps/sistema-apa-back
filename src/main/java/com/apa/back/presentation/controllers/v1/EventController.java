package com.apa.back.presentation.controllers.v1;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events/v1")
public class EventController {

    @PostMapping
    public void createEvent() {



    }

}
