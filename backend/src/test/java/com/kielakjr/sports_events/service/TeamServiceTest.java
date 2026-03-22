package com.kielakjr.sports_events.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import com.kielakjr.sports_events.repo.TeamRepo;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

  @Mock
  private TeamRepo teamRepo;

  @InjectMocks
  private TeamService teamService;

  private Sport football;
  private Team salzburg;
  private Team sturm;

  @BeforeEach
  void setUp() {
    football = new Sport(1L, "Football");
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
        .isInstanceOf(jakarta.persistence.EntityNotFoundException.class)
        .hasMessageContaining("99");
  }

  @Test
  void createTeam_savesAndReturnsTeam() {
    Team newTeam = new Team(null, "KAC", "EC KAC", "KAC", "AT", football);
    Team savedTeam = new Team(3L, "KAC", "EC KAC", "KAC", "AT", football);

    when(teamRepo.save(any(Team.class))).thenReturn(savedTeam);

    Team result = teamService.createTeam(newTeam);

    assertThat(result.getId()).isEqualTo(3L);
    assertThat(result.getName()).isEqualTo("KAC");
    verify(teamRepo).save(newTeam);
  }
}
