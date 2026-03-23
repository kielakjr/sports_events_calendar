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
import com.kielakjr.sports_events.model.Venue;
import com.kielakjr.sports_events.service.VenueService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(controllers = VenueController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class VenueControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private VenueService venueService;

  private final Venue arena = new Venue(1L, "Red Bull Arena", "Salzburg");
  private final Venue stadion = new Venue(2L, "Merkur Arena", "Graz");

  @Test
  void getAllVenues_returnsOkWithList() throws Exception {
    when(venueService.getAllVenues()).thenReturn(List.of(arena, stadion));

    mockMvc.perform(get("/api/venues"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Red Bull Arena"))
        .andExpect(jsonPath("$[0].city").value("Salzburg"))
        .andExpect(jsonPath("$[1].name").value("Merkur Arena"));
  }

  @Test
  void getAllVenues_emptyList() throws Exception {
    when(venueService.getAllVenues()).thenReturn(List.of());

    mockMvc.perform(get("/api/venues"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getVenueById_returnsOk() throws Exception {
    when(venueService.getVenueById(1L)).thenReturn(arena);

    mockMvc.perform(get("/api/venues/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Red Bull Arena"))
        .andExpect(jsonPath("$.city").value("Salzburg"));
  }

  @Test
  void getVenueById_notFound() throws Exception {
    when(venueService.getVenueById(99L))
        .thenThrow(new EntityNotFoundException("Venue not found with id: 99"));

    mockMvc.perform(get("/api/venues/99"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createVenue_returnsCreated() throws Exception {
    when(venueService.createVenue(any(Venue.class)))
        .thenReturn(new Venue(3L, "Heidi Horten Arena", "Klagenfurt"));

    mockMvc.perform(post("/api/venues")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Heidi Horten Arena", "city": "Klagenfurt" }
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("Heidi Horten Arena"))
        .andExpect(jsonPath("$.city").value("Klagenfurt"));
  }

  @Test
  void updateVenue_returnsOk() throws Exception {
    when(venueService.updateVenue(eq(1L), any(Venue.class)))
        .thenReturn(new Venue(1L, "Red Bull Arena Salzburg", "Salzburg"));

    mockMvc.perform(put("/api/venues/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Red Bull Arena Salzburg", "city": "Salzburg" }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Red Bull Arena Salzburg"));
  }

  @Test
  void updateVenue_notFound() throws Exception {
    when(venueService.updateVenue(eq(99L), any(Venue.class)))
        .thenThrow(new EntityNotFoundException("Venue not found with id: 99"));

    mockMvc.perform(put("/api/venues/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Test", "city": "Test" }
                """))
        .andExpect(status().isNotFound());
  }

  @Test
  void createVenue_invalidBody_returnsBadRequest() throws Exception {
    mockMvc.perform(post("/api/venues")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "", "city": "" }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name").value("Name is mandatory"))
        .andExpect(jsonPath("$.city").value("City is mandatory"));
  }

  @Test
  void deleteVenue_returnsNoContent() throws Exception {
    doNothing().when(venueService).deleteVenue(1L);

    mockMvc.perform(delete("/api/venues/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteVenue_conflict() throws Exception {
    doThrow(new IllegalStateException("Cannot delete venue with associated events"))
        .when(venueService).deleteVenue(1L);

    mockMvc.perform(delete("/api/venues/1"))
        .andExpect(status().isConflict());
  }

  @Test
  void deleteVenue_notFound() throws Exception {
    doThrow(new EntityNotFoundException("Venue not found with id: 99"))
        .when(venueService).deleteVenue(99L);

    mockMvc.perform(delete("/api/venues/99"))
        .andExpect(status().isNotFound());
  }
}
