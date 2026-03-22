package com.kielakjr.sports_events.controller;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

@WebMvcTest(controllers = TeamController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class TeamControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TeamService teamService;

  private final Sport football = new Sport(1L, "Football");
  private Team salzburg = new Team(1L, "Salzburg", "FC Red Bull Salzburg", "SAL", "AT", football);
  private Team sturm = new Team(2L, "Sturm", "SK Sturm Graz", "STU", "AT", football);


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
        .thenThrow(new jakarta.persistence.EntityNotFoundException("Team not found with id: 99"));

    mockMvc.perform(get("/api/teams/99"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createTeam_returnsCreatedTeam() throws Exception {
    Team savedTeam = new Team(3L, "KAC", "EC KAC", "KAC", "AT", football);
    when(teamService.createTeam(any(Team.class))).thenReturn(savedTeam);

    String json = """
        {
          "name": "KAC",
          "officialName": "EC KAC",
          "abbreviation": "KAC",
          "teamCountryCode": "AT",
          "sport": { "id": 1, "name": "Football" }
        }
        """;

    mockMvc.perform(post("/api/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("KAC"))
        .andExpect(jsonPath("$.abbreviation").value("KAC"));
  }
}
