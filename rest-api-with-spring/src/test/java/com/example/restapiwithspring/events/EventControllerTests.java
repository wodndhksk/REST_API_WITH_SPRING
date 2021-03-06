package com.example.restapiwithspring.events;

import com.example.restapiwithspring.common.RestDocsConfiguration;
import com.example.restapiwithspring.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({RestDocsConfiguration.class})
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("??????????????? ???????????? ???????????? ?????????")
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
                .location("????????? ????????? M??????")
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
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))

                .andDo(document("create-event",
                            links(
                                    linkWithRel("self").description("link to self")
                                    ,linkWithRel("query-events").description("link to query events")
                                    ,linkWithRel("update-event").description("link to update and existing")
                                    ,linkWithRel("profile").description("link to update and existing")
                            ),
                            requestHeaders(
                                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                            ),
                            requestFields(
                                    fieldWithPath("name").description("Name of new evnet"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                    fieldWithPath("location").description("location of new event"),
                                    fieldWithPath("basePrice").description("base price of new event"),
                                    fieldWithPath("maxPrice").description("max price of new event"),
                                    fieldWithPath("limitOfEnrollment").description("limit of new event")
                            ),
                            responseHeaders(
                                    headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                            ),
                        // relaxedResponseFields : ????????? ????????? ????????? ????????? ????????? ??? ???????????? ????????? ???????????? (??????: ?????? ???????????? ????????? ??? ??? ??????, ??????: ????????? ????????? ????????? ??????)
                            responseFields( // ???????????? ???????????? ??? ???????????? ???????????? ?????????
                                    fieldWithPath("id").description("identifier of new event"),
                                    fieldWithPath("name").description("Name of new evnet"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                    fieldWithPath("location").description("location of new event"),
                                    fieldWithPath("basePrice").description("base price of new event"),
                                    fieldWithPath("maxPrice").description("max price of new event"),
                                    fieldWithPath("limitOfEnrollment").description("limit of new event"),
                                    fieldWithPath("free").description("it tells if this event is free or not"),
                                    fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                    fieldWithPath("eventStatus").description("event status"),
                                    fieldWithPath("_links.self.href").description("link to self"),
                                    fieldWithPath("_links.query-events.href").description("link to query event list"),
                                    fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                    fieldWithPath("_links.profile.href").description("link to profile")
                            )

                        ))
        ;  // 201
    }
    @Test
    @TestDescription("?????? ?????? ??? ?????? ?????? ????????? ????????? ????????? ???????????? ?????????")
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
                .location("????????? ????????? M??????")
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
    @TestDescription("?????? ?????? ???????????? ????????? ????????? ???????????? ?????????")
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
    @TestDescription("?????? ?????? ????????? ????????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Development with Spring")
                // ?????? ??? ??????
                .beginEnrollmentDateTime(LocalDateTime.of(2022,06, 30, 10, 49))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,06, 29, 10, 49))
                .beginEventDateTime(LocalDateTime.of(2022, 06, 28, 10, 49))
                .endEventDateTime(LocalDateTime.of(2022, 06, 27, 10, 49))
                //base ?????? max ?????? ?????? (?????? case : base < max )
                .basePrice(10000)
                .maxPrice(200)
                //
                .limitOfEnrollment(100)
                .location("????????? ????????? M??????")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
//                .andExpect(jsonPath("$[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }
}
