package com.kielakjr.sports_events.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kielakjr.sports_events.model.Event;
import com.kielakjr.sports_events.model.Venue;
import com.kielakjr.sports_events.repo.EventRepo;
import com.kielakjr.sports_events.repo.VenueRepo;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

  @Mock
  private VenueRepo venueRepo;

  @Mock
  private EventRepo eventRepo;

  @InjectMocks
  private VenueService venueService;

  private Venue arena;
  private Venue stadion;

  @BeforeEach
  void setUp() {
    arena = new Venue(1L, "Red Bull Arena", "Salzburg");
    stadion = new Venue(2L, "Merkur Arena", "Graz");
  }

  @Test
  void getAllVenues_returnsList() {
    when(venueRepo.findAll()).thenReturn(List.of(arena, stadion));

    List<Venue> result = venueService.getAllVenues();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getName()).isEqualTo("Red Bull Arena");
    assertThat(result.get(1).getCity()).isEqualTo("Graz");
    verify(venueRepo).findAll();
  }

  @Test
  void getAllVenues_emptyList() {
    when(venueRepo.findAll()).thenReturn(List.of());

    List<Venue> result = venueService.getAllVenues();

    assertThat(result).isEmpty();
  }

  @Test
  void getVenueById_returnsVenue() {
    when(venueRepo.findById(1L)).thenReturn(Optional.of(arena));

    Venue result = venueService.getVenueById(1L);

    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Red Bull Arena");
    assertThat(result.getCity()).isEqualTo("Salzburg");
  }

  @Test
  void getVenueById_notFound_throwsException() {
    when(venueRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> venueService.getVenueById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");
  }

  @Test
  void createVenue_savesAndReturns() {
    Venue newVenue = new Venue(null, "Heidi Horten Arena", "Klagenfurt");
    Venue savedVenue = new Venue(3L, "Heidi Horten Arena", "Klagenfurt");
    when(venueRepo.save(any(Venue.class))).thenReturn(savedVenue);

    Venue result = venueService.createVenue(newVenue);

    assertThat(result.getId()).isEqualTo(3L);
    assertThat(result.getName()).isEqualTo("Heidi Horten Arena");
    verify(venueRepo).save(newVenue);
  }

  @Test
  void updateVenue_updatesAndReturns() {
    Venue updated = new Venue(1L, "Red Bull Arena Salzburg", "Salzburg");
    when(venueRepo.findById(1L)).thenReturn(Optional.of(arena));
    when(venueRepo.save(any(Venue.class))).thenReturn(updated);

    Venue result = venueService.updateVenue(1L, new Venue(null, "Red Bull Arena Salzburg", "Salzburg"));

    assertThat(result.getName()).isEqualTo("Red Bull Arena Salzburg");
    verify(venueRepo).save(arena);
  }

  @Test
  void updateVenue_notFound_throwsException() {
    when(venueRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> venueService.updateVenue(99L, new Venue(null, "Test", "Test")))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(venueRepo, never()).save(any());
  }

  @Test
  void deleteVenue_deletes() {
    when(venueRepo.existsById(1L)).thenReturn(true);
    when(eventRepo.findByVenueId(1L)).thenReturn(List.of());

    venueService.deleteVenue(1L);

    verify(venueRepo).deleteById(1L);
  }

  @Test
  void deleteVenue_conflict_throwsException() {
    when(venueRepo.existsById(1L)).thenReturn(true);
    when(eventRepo.findByVenueId(1L)).thenReturn(List.of(new Event()));

    assertThatThrownBy(() -> venueService.deleteVenue(1L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Cannot delete venue");

    verify(venueRepo, never()).deleteById(any());
  }

  @Test
  void deleteVenue_notFound_throwsException() {
    when(venueRepo.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> venueService.deleteVenue(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(venueRepo, never()).deleteById(any());
  }
}
