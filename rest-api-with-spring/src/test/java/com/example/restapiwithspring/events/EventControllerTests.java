package com.example.restapiwithspring.events;

import com.example.restapiwithspring.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,06, 27, 10, 49))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,06, 28, 10, 49))
                .beginEventDateTime(LocalDateTime.of(2022, 06, 29, 10, 49))
                .endEventDateTime(LocalDateTime.of(2022, 06, 30, 10, 49))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("뚝섬역 서울숲 M타워")
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;  // 201
    }
    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(10)
                .name("Spring")
                .description("Rest API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,06, 27, 10, 49))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,06, 28, 10, 49))
                .beginEventDateTime(LocalDateTime.of(2022, 06, 29, 10, 49))
                .endEventDateTime(LocalDateTime.of(2022, 06, 30, 10, 49))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("뚝섬역 서울숲 M타워")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Development with Spring")
                // 날짜 값 오류
                .beginEnrollmentDateTime(LocalDateTime.of(2022,06, 30, 10, 49))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,06, 29, 10, 49))
                .beginEventDateTime(LocalDateTime.of(2022, 06, 28, 10, 49))
                .endEventDateTime(LocalDateTime.of(2022, 06, 27, 10, 49))
                //base 값과 max 값의 오류 (정상 case : base < max )
                .basePrice(10000)
                .maxPrice(200)
                //
                .limitOfEnrollment(100)
                .location("뚝섬역 서울숲 M타워")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest())
        ;
    }
}
