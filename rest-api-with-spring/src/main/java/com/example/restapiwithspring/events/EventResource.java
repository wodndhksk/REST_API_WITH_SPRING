package com.example.restapiwithspring.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
//BeanSerializer 사용
public class EventResource extends EntityModel<Event> {
//    public EventResource(Event content, Link... links){
//        super(content, links);
//    }

    @JsonUnwrapped
    private Event event;

    public EventResource(Event event){
        this.event = event;
    }
    public Event getEvent(){
        return event;
    }
}
