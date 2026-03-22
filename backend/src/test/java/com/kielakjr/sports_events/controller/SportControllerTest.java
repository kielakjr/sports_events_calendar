package com.kielakjr.sports_events.controller;

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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.kielakjr.sports_events.config.GlobalExceptionHandler;
import com.kielakjr.sports_events.config.SecurityConfig;
import com.kielakjr.sports_events.model.Sport;
import com.kielakjr.sports_events.service.SportService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = SportController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class SportControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SportService sportService;

  private final Sport football = new Sport(1L, "Football");
  private final Sport hockey = new Sport(2L, "Ice Hockey");


  @Test
  void getAllSports_returnsOkWithList() throws Exception {
    when(sportService.getAllSports()).thenReturn(List.of(football, hockey));

    mockMvc.perform(get("/api/sports"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Football"))
        .andExpect(jsonPath("$[1].name").value("Ice Hockey"));
  }

  @Test
  void getAllSports_emptyList() throws Exception {
    when(sportService.getAllSports()).thenReturn(List.of());

    mockMvc.perform(get("/api/sports"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getSportById_returnsOk() throws Exception {
    when(sportService.getSportById(1L)).thenReturn(football);

    mockMvc.perform(get("/api/sports/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Football"));
  }

  @Test
  void getSportById_notFound() throws Exception {
    when(sportService.getSportById(99L))
        .thenThrow(new EntityNotFoundException("Sport not found with id: 99"));

    mockMvc.perform(get("/api/sports/99"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createSport_returnsCreated() throws Exception {
    when(sportService.createSport(any(Sport.class))).thenReturn(new Sport(3L, "Tennis"));

    mockMvc.perform(post("/api/sports")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Tennis" }
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("Tennis"));
  }

  @Test
  void updateSport_returnsOk() throws Exception {
    when(sportService.updateSport(eq(1L), any(Sport.class))).thenReturn(new Sport(1L, "Soccer"));

    mockMvc.perform(put("/api/sports/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Soccer" }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Soccer"));
  }

  @Test
  void updateSport_notFound() throws Exception {
    when(sportService.updateSport(eq(99L), any(Sport.class)))
        .thenThrow(new EntityNotFoundException("Sport not found with id: 99"));

    mockMvc.perform(put("/api/sports/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Tennis" }
                """))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteSport_returnsNoContent() throws Exception {
    doNothing().when(sportService).deleteSport(1L);

    mockMvc.perform(delete("/api/sports/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteSport_notFound() throws Exception {
    doThrow(new EntityNotFoundException("Sport not found with id: 99"))
        .when(sportService).deleteSport(99L);

    mockMvc.perform(delete("/api/sports/99"))
        .andExpect(status().isNotFound());
  }
}
