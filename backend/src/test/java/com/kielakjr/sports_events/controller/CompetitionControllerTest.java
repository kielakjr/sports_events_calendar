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
import com.kielakjr.sports_events.model.Competition;
import com.kielakjr.sports_events.service.CompetitionService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = CompetitionController.class)
@Import(GlobalExceptionHandler.class)
class CompetitionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CompetitionService competitionService;

  private final Competition bundesliga = new Competition(1L, "Bundesliga");
  private final Competition iceLeague = new Competition(2L, "ICE Hockey League");

  @Test
  void getAllCompetitions_returnsOkWithList() throws Exception {
    when(competitionService.getAllCompetitions()).thenReturn(List.of(bundesliga, iceLeague));

    mockMvc.perform(get("/api/competitions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Bundesliga"))
        .andExpect(jsonPath("$[1].name").value("ICE Hockey League"));
  }

  @Test
  void getAllCompetitions_emptyList() throws Exception {
    when(competitionService.getAllCompetitions()).thenReturn(List.of());

    mockMvc.perform(get("/api/competitions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getCompetitionById_returnsOk() throws Exception {
    when(competitionService.getCompetitionById(1L)).thenReturn(bundesliga);

    mockMvc.perform(get("/api/competitions/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Bundesliga"));
  }

  @Test
  void getCompetitionById_notFound() throws Exception {
    when(competitionService.getCompetitionById(99L))
        .thenThrow(new EntityNotFoundException("Competition not found with id: 99"));

    mockMvc.perform(get("/api/competitions/99"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createCompetition_returnsCreated() throws Exception {
    when(competitionService.createCompetition(any(Competition.class)))
        .thenReturn(new Competition(3L, "Champions League"));

    mockMvc.perform(post("/api/competitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Champions League" }
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("Champions League"));
  }

  @Test
  void updateCompetition_returnsOk() throws Exception {
    when(competitionService.updateCompetition(eq(1L), any(Competition.class)))
        .thenReturn(new Competition(1L, "Austrian Bundesliga"));

    mockMvc.perform(put("/api/competitions/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Austrian Bundesliga" }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Austrian Bundesliga"));
  }

  @Test
  void updateCompetition_notFound() throws Exception {
    when(competitionService.updateCompetition(eq(99L), any(Competition.class)))
        .thenThrow(new EntityNotFoundException("Competition not found with id: 99"));

    mockMvc.perform(put("/api/competitions/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Test" }
                """))
        .andExpect(status().isNotFound());
  }

  @Test
  void createCompetition_invalidBody_returnsBadRequest() throws Exception {
    mockMvc.perform(post("/api/competitions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "" }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name").value("Name is mandatory"));
  }

  @Test
  void deleteCompetition_returnsNoContent() throws Exception {
    doNothing().when(competitionService).deleteCompetition(1L);

    mockMvc.perform(delete("/api/competitions/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteCompetition_conflict() throws Exception {
    doThrow(new IllegalStateException("Cannot delete competition with associated events"))
        .when(competitionService).deleteCompetition(1L);

    mockMvc.perform(delete("/api/competitions/1"))
        .andExpect(status().isConflict());
  }

  @Test
  void deleteCompetition_notFound() throws Exception {
    doThrow(new EntityNotFoundException("Competition not found with id: 99"))
        .when(competitionService).deleteCompetition(99L);

    mockMvc.perform(delete("/api/competitions/99"))
        .andExpect(status().isNotFound());
  }
}
