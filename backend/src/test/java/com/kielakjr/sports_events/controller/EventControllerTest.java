package com.kielakjr.sports_events.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.kielakjr.sports_events.config.GlobalExceptionHandler;
import com.kielakjr.sports_events.config.SecurityConfig;
import com.kielakjr.sports_events.dto.EventResponseDTO;
import com.kielakjr.sports_events.service.EventService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = EventController.class)
@org.springframework.context.annotation.Import({SecurityConfig.class, GlobalExceptionHandler.class})
class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private EventService eventService;

  private EventResponseDTO sampleEvent() {
    return EventResponseDTO.builder()
        .id(1L)
        .eventDate(LocalDate.of(2019, 7, 18))
        .eventTime(LocalTime.of(18, 30))
        .season("2019/2020")
        .status("SCHEDULED")
        .stage("Regular Season")
        .competitionName("Bundesliga")
        .venueName("Red Bull Arena")
        .venueCity("Salzburg")
        .sportName("Football")
        .participants(List.of(
            EventResponseDTO.ParticipantInfo.builder()
                .teamId(1L).teamName("Salzburg").abbreviation("SAL").role("HOME").build(),
            EventResponseDTO.ParticipantInfo.builder()
                .teamId(2L).teamName("Sturm").abbreviation("STU").role("AWAY").build()))
        .results(List.of())
        .build();
  }

  @Test
  void getAllEvents_returnsOkWithList() throws Exception {
    when(eventService.getAllEvents()).thenReturn(List.of(sampleEvent()));

    mockMvc.perform(get("/api/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].competitionName").value("Bundesliga"))
        .andExpect(jsonPath("$[0].participants.length()").value(2));
  }

  @Test
  void getAllEvents_emptyList() throws Exception {
    when(eventService.getAllEvents()).thenReturn(List.of());

    mockMvc.perform(get("/api/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getEventById_returnsOk() throws Exception {
    when(eventService.getEventById(1L)).thenReturn(sampleEvent());

    mockMvc.perform(get("/api/events/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.venueName").value("Red Bull Arena"))
        .andExpect(jsonPath("$.sportName").value("Football"));
  }

  @Test
  void getEventById_notFound() throws Exception {
    when(eventService.getEventById(99L))
        .thenThrow(new EntityNotFoundException("Event not found with id: 99"));

    mockMvc.perform(get("/api/events/99"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createEvent_returnsCreated() throws Exception {
    when(eventService.createEvent(any())).thenReturn(sampleEvent());

    String json = """
        {
          "eventDate": "2019-07-18",
          "eventTime": "18:30:00",
          "season": "2019/2020",
          "status": "SCHEDULED",
          "stage": "Regular Season",
          "competitionId": 1,
          "venueId": 1,
          "participants": [
            {"teamId": 1, "role": "HOME"},
            {"teamId": 2, "role": "AWAY"}
          ]
        }
        """;

    mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.competitionName").value("Bundesliga"));
  }

  @Test
  void updateEvent_returnsOk() throws Exception {
    when(eventService.updateEvent(eq(1L), any())).thenReturn(sampleEvent());

    String json = """
        {
          "eventDate": "2019-07-18",
          "eventTime": "18:30:00",
          "season": "2019/2020",
          "status": "SCHEDULED",
          "stage": "Regular Season",
          "competitionId": 1,
          "venueId": 1,
          "participants": [
            {"teamId": 1, "role": "HOME"},
            {"teamId": 2, "role": "AWAY"}
          ]
        }
        """;

    mockMvc.perform(put("/api/events/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void deleteEvent_returnsNoContent() throws Exception {
    doNothing().when(eventService).deleteEvent(1L);

    mockMvc.perform(delete("/api/events/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteEvent_notFound() throws Exception {
    doThrow(new EntityNotFoundException("Event not found with id: 99"))
        .when(eventService).deleteEvent(99L);

    mockMvc.perform(delete("/api/events/99"))
        .andExpect(status().isNotFound());
  }
}
