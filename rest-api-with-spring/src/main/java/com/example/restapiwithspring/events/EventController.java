package com.example.restapiwithspring.events;

import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * 입력값들을 전달하면 JSON 응답으로 201이 나오는지 확인.
     * Location 헤더에 생성된 이벤트를 조회할 수 있는 URI 담겨 있는지 확인.
     * @return
     */
    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto){
        Event event = modelMapper.map(eventDto, Event.class); // Dto 값들을 Event에 대입 (builder나 set을 사용하지 않아도 됨)
        Event newEvenv = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvenv.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);
    }
}
