package com.example.restapiwithspring.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * 입력값들을 전달하면 JSON 응답으로 201이 나오는지 확인.
     * Location 헤더에 생성된 이벤트를 조회할 수 있는 URI 담겨 있는지 확인.
     * @return
     */
    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event){
        Event newEvenv = this.eventRepository.save(event);

        URI createUri = linkTo(EventController.class).slash(newEvenv.getId()).toUri();
        event.setId(10);
        return ResponseEntity.created(createUri).body(event);
    }
}
