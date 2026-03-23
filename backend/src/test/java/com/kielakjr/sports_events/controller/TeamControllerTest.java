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
import com.kielakjr.sports_events.model.Team;
import com.kielakjr.sports_events.service.TeamService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = TeamController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class TeamControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TeamService teamService;

  private final Sport football = new Sport(1L, "Football");
  private final Team salzburg = new Team(1L, "Salzburg", "FC Red Bull Salzburg", "SAL", "AUT", football);
  private final Team sturm = new Team(2L, "Sturm", "SK Sturm Graz", "STU", "AUT", football);

  @Test
  void getAllTeams_returnsOkWithList() throws Exception {
    when(teamService.getAllTeams()).thenReturn(List.of(salzburg, sturm));

    mockMvc.perform(get("/api/teams"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Salzburg"))
        .andExpect(jsonPath("$[0].abbreviation").value("SAL"))
        .andExpect(jsonPath("$[1].name").value("Sturm"));
  }

  @Test
  void getAllTeams_emptyList() throws Exception {
    when(teamService.getAllTeams()).thenReturn(List.of());

    mockMvc.perform(get("/api/teams"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getTeamById_returnsOk() throws Exception {
    when(teamService.getTeamById(1L)).thenReturn(salzburg);

    mockMvc.perform(get("/api/teams/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Salzburg"))
        .andExpect(jsonPath("$.officialName").value("FC Red Bull Salzburg"))
        .andExpect(jsonPath("$.sport.name").value("Football"));
  }

  @Test
  void getTeamById_notFound() throws Exception {
    when(teamService.getTeamById(99L))
        .thenThrow(new EntityNotFoundException("Team not found with id: 99"));

    mockMvc.perform(get("/api/teams/99"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createTeam_returnsCreated() throws Exception {
    Team savedTeam = new Team(3L, "KAC", "EC KAC", "KAC", "AUT", football);
    when(teamService.createTeam(any(Team.class))).thenReturn(savedTeam);

    mockMvc.perform(post("/api/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "KAC",
                  "officialName": "EC KAC",
                  "abbreviation": "KAC",
                  "teamCountryCode": "AUT",
                  "sport": { "id": 1 }
                }
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("KAC"))
        .andExpect(jsonPath("$.abbreviation").value("KAC"));
  }

  @Test
  void updateTeam_returnsOk() throws Exception {
    Team updated = new Team(1L, "RB Salzburg", "FC Red Bull Salzburg", "RBS", "AUT", football);
    when(teamService.updateTeam(eq(1L), any(Team.class))).thenReturn(updated);

    mockMvc.perform(put("/api/teams/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "RB Salzburg",
                  "officialName": "FC Red Bull Salzburg",
                  "abbreviation": "RBS",
                  "teamCountryCode": "AUT",
                  "sport": { "id": 1 }
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("RB Salzburg"))
        .andExpect(jsonPath("$.abbreviation").value("RBS"));
  }

  @Test
  void updateTeam_notFound() throws Exception {
    when(teamService.updateTeam(eq(99L), any(Team.class)))
        .thenThrow(new EntityNotFoundException("Team not found with id: 99"));

    mockMvc.perform(put("/api/teams/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "Test",
                  "officialName": "Test",
                  "abbreviation": "TST",
                  "teamCountryCode": "AUT",
                  "sport": { "id": 1 }
                }
                """))
        .andExpect(status().isNotFound());
  }

  @Test
  void createTeam_invalidBody_returnsBadRequest() throws Exception {
    mockMvc.perform(post("/api/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "",
                  "officialName": "",
                  "abbreviation": "",
                  "teamCountryCode": "XX"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name").value("Name is mandatory"))
        .andExpect(jsonPath("$.officialName").value("Official name is mandatory"))
        .andExpect(jsonPath("$.abbreviation").value("Abbreviation is mandatory"))
        .andExpect(jsonPath("$.sport").value("Sport is mandatory"));
  }

  @Test
  void createTeam_invalidCountryCode_returnsBadRequest() throws Exception {
    mockMvc.perform(post("/api/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "Test",
                  "officialName": "Test FC",
                  "abbreviation": "TST",
                  "teamCountryCode": "AT",
                  "sport": { "id": 1 }
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.teamCountryCode").value("Country code must be exactly 3 characters"));
  }

  @Test
  void deleteTeam_returnsNoContent() throws Exception {
    doNothing().when(teamService).deleteTeam(1L);

    mockMvc.perform(delete("/api/teams/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteTeam_conflict() throws Exception {
    doThrow(new IllegalStateException("Cannot delete team with id: 1 because it is associated with existing events"))
        .when(teamService).deleteTeam(1L);

    mockMvc.perform(delete("/api/teams/1"))
        .andExpect(status().isConflict());
  }

  @Test
  void deleteTeam_notFound() throws Exception {
    doThrow(new EntityNotFoundException("Team not found with id: 99"))
        .when(teamService).deleteTeam(99L);

    mockMvc.perform(delete("/api/teams/99"))
        .andExpect(status().isNotFound());
  }
}
