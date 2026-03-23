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

import com.kielakjr.sports_events.model.Sport;
import com.kielakjr.sports_events.model.Team;
import com.kielakjr.sports_events.repo.SportRepo;
import com.kielakjr.sports_events.repo.TeamRepo;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class SportServiceTest {

  @Mock
  private SportRepo sportRepo;

  @Mock
  private TeamRepo teamRepo;

  @InjectMocks
  private SportService sportService;

  private Sport football;
  private Sport hockey;

  @BeforeEach
  void setUp() {
    football = new Sport(1L, "Football");
    hockey = new Sport(2L, "Ice Hockey");
  }

  @Test
  void getAllSports_returnsList() {
    when(sportRepo.findAll()).thenReturn(List.of(football, hockey));

    List<Sport> result = sportService.getAllSports();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getName()).isEqualTo("Football");
    assertThat(result.get(1).getName()).isEqualTo("Ice Hockey");
    verify(sportRepo).findAll();
  }

  @Test
  void getAllSports_emptyList() {
    when(sportRepo.findAll()).thenReturn(List.of());

    List<Sport> result = sportService.getAllSports();

    assertThat(result).isEmpty();
  }

  @Test
  void getSportById_returnsSport() {
    when(sportRepo.findById(1L)).thenReturn(Optional.of(football));

    Sport result = sportService.getSportById(1L);

    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Football");
  }

  @Test
  void getSportById_notFound_throwsException() {
    when(sportRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> sportService.getSportById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");
  }

  @Test
  void createSport_savesAndReturns() {
    Sport newSport = new Sport(null, "Tennis");
    Sport savedSport = new Sport(3L, "Tennis");
    when(sportRepo.save(any(Sport.class))).thenReturn(savedSport);

    Sport result = sportService.createSport(newSport);

    assertThat(result.getId()).isEqualTo(3L);
    assertThat(result.getName()).isEqualTo("Tennis");
    verify(sportRepo).save(newSport);
  }

  @Test
  void updateSport_updatesAndReturns() {
    Sport existing = new Sport(1L, "Football");
    Sport updated = new Sport(1L, "Soccer");
    when(sportRepo.findById(1L)).thenReturn(Optional.of(existing));
    when(sportRepo.save(any(Sport.class))).thenReturn(updated);

    Sport result = sportService.updateSport(1L, new Sport(null, "Soccer"));

    assertThat(result.getName()).isEqualTo("Soccer");
    verify(sportRepo).save(existing);
  }

  @Test
  void updateSport_notFound_throwsException() {
    when(sportRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> sportService.updateSport(99L, new Sport(null, "Tennis")))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(sportRepo, never()).save(any());
  }

  @Test
  void deleteSport_deletes() {
    when(sportRepo.existsById(1L)).thenReturn(true);
    when(teamRepo.findBySportId(1L)).thenReturn(List.of());

    sportService.deleteSport(1L);

    verify(sportRepo).deleteById(1L);
  }

  @Test
  void deleteSport_conflict_throwsException() {
    when(sportRepo.existsById(1L)).thenReturn(true);
    when(teamRepo.findBySportId(1L)).thenReturn(List.of(new Team()));

    assertThatThrownBy(() -> sportService.deleteSport(1L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Cannot delete sport");

    verify(sportRepo, never()).deleteById(any());
  }

  @Test
  void deleteSport_notFound_throwsException() {
    when(sportRepo.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> sportService.deleteSport(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(sportRepo, never()).deleteById(any());
  }
}
