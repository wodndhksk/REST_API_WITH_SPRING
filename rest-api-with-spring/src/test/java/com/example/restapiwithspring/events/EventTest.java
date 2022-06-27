package com.example.restapiwithspring.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {
    @Test
    public void builder(){
        Event event = Event.builder()
                .name("SPRING REST API TEST")
                .description("REST API STUDY")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        Event event = new Event();
        String name = "Event";
        String spring = "Spring";

        event.setName(name);
        event.setDescription("Event Spring");

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(spring);
    }
}