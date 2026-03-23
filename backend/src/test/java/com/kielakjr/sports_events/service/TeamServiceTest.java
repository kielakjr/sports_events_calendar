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
import com.kielakjr.sports_events.model.Sport;
import com.kielakjr.sports_events.model.Team;
import com.kielakjr.sports_events.repo.EventRepo;
import com.kielakjr.sports_events.repo.SportRepo;
import com.kielakjr.sports_events.repo.TeamRepo;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

  @Mock
  private TeamRepo teamRepo;

  @Mock
  private SportRepo sportRepo;

  @Mock
  private EventRepo eventRepo;

  @InjectMocks
  private TeamService teamService;

  private Sport football;
  private Sport hockey;
  private Team salzburg;
  private Team sturm;

  @BeforeEach
  void setUp() {
    football = new Sport(1L, "Football");
    hockey = new Sport(2L, "Ice Hockey");
    salzburg = new Team(1L, "Salzburg", "FC Red Bull Salzburg", "SAL", "AT", football);
    sturm = new Team(2L, "Sturm", "SK Sturm Graz", "STU", "AT", football);
  }

  @Test
  void getAllTeams_returnsList() {
    when(teamRepo.findAll()).thenReturn(List.of(salzburg, sturm));

    List<Team> result = teamService.getAllTeams();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getName()).isEqualTo("Salzburg");
    assertThat(result.get(1).getName()).isEqualTo("Sturm");
    verify(teamRepo).findAll();
  }

  @Test
  void getAllTeams_emptyList() {
    when(teamRepo.findAll()).thenReturn(List.of());

    List<Team> result = teamService.getAllTeams();

    assertThat(result).isEmpty();
  }

  @Test
  void getTeamById_returnsTeam() {
    when(teamRepo.findById(1L)).thenReturn(Optional.of(salzburg));

    Team result = teamService.getTeamById(1L);

    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Salzburg");
    assertThat(result.getOfficialName()).isEqualTo("FC Red Bull Salzburg");
    assertThat(result.getSport().getName()).isEqualTo("Football");
  }

  @Test
  void getTeamById_notFound_throwsException() {
    when(teamRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> teamService.getTeamById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");
  }

  @Test
  void createTeam_savesAndReturnsTeam() {
    Team newTeam = new Team(null, "KAC", "EC KAC", "KAC", "AT", new Sport(2L, null));
    Team savedTeam = new Team(3L, "KAC", "EC KAC", "KAC", "AT", hockey);
    when(sportRepo.findById(2L)).thenReturn(Optional.of(hockey));
    when(teamRepo.save(any(Team.class))).thenReturn(savedTeam);

    Team result = teamService.createTeam(newTeam);

    assertThat(result.getId()).isEqualTo(3L);
    assertThat(result.getName()).isEqualTo("KAC");
    assertThat(result.getSport().getName()).isEqualTo("Ice Hockey");
    verify(teamRepo).save(newTeam);
  }

  @Test
  void createTeam_sportNotFound_throwsException() {
    Team newTeam = new Team(null, "KAC", "EC KAC", "KAC", "AT", new Sport(99L, null));
    when(sportRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> teamService.createTeam(newTeam))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(teamRepo, never()).save(any());
  }

  @Test
  void updateTeam_updatesAndReturns() {
    Team updated = new Team(1L, "RB Salzburg", "FC Red Bull Salzburg", "RBS", "AT", hockey);
    when(teamRepo.findById(1L)).thenReturn(Optional.of(salzburg));
    when(sportRepo.findById(2L)).thenReturn(Optional.of(hockey));
    when(teamRepo.save(any(Team.class))).thenReturn(updated);

    Team details = new Team(null, "RB Salzburg", "FC Red Bull Salzburg", "RBS", "AT", new Sport(2L, null));
    Team result = teamService.updateTeam(1L, details);

    assertThat(result.getName()).isEqualTo("RB Salzburg");
    assertThat(result.getAbbreviation()).isEqualTo("RBS");
    assertThat(result.getSport().getName()).isEqualTo("Ice Hockey");
    verify(teamRepo).save(salzburg);
  }

  @Test
  void updateTeam_notFound_throwsException() {
    when(teamRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> teamService.updateTeam(99L, new Team()))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(teamRepo, never()).save(any());
  }

  @Test
  void deleteTeam_deletes() {
    when(teamRepo.existsById(1L)).thenReturn(true);
    when(eventRepo.findByTeamId(1L)).thenReturn(List.of());

    teamService.deleteTeam(1L);

    verify(teamRepo).deleteById(1L);
  }

  @Test
  void deleteTeam_conflict_throwsException() {
    when(teamRepo.existsById(1L)).thenReturn(true);
    when(eventRepo.findByTeamId(1L)).thenReturn(List.of(new Event()));

    assertThatThrownBy(() -> teamService.deleteTeam(1L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Cannot delete team");

    verify(teamRepo, never()).deleteById(any());
  }

  @Test
  void deleteTeam_notFound_throwsException() {
    when(teamRepo.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> teamService.deleteTeam(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(teamRepo, never()).deleteById(any());
  }
}
