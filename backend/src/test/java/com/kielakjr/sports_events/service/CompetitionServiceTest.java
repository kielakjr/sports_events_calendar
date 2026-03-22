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

import com.kielakjr.sports_events.model.Competition;
import com.kielakjr.sports_events.repo.CompetitionRepo;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceTest {

  @Mock
  private CompetitionRepo competitionRepo;

  @InjectMocks
  private CompetitionService competitionService;

  private Competition bundesliga;
  private Competition iceLeague;

  @BeforeEach
  void setUp() {
    bundesliga = new Competition(1L, "Bundesliga");
    iceLeague = new Competition(2L, "ICE Hockey League");
  }

  @Test
  void getAllCompetitions_returnsList() {
    when(competitionRepo.findAll()).thenReturn(List.of(bundesliga, iceLeague));

    List<Competition> result = competitionService.getAllCompetitions();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getName()).isEqualTo("Bundesliga");
    assertThat(result.get(1).getName()).isEqualTo("ICE Hockey League");
    verify(competitionRepo).findAll();
  }

  @Test
  void getAllCompetitions_emptyList() {
    when(competitionRepo.findAll()).thenReturn(List.of());

    List<Competition> result = competitionService.getAllCompetitions();

    assertThat(result).isEmpty();
  }

  @Test
  void getCompetitionById_returnsCompetition() {
    when(competitionRepo.findById(1L)).thenReturn(Optional.of(bundesliga));

    Competition result = competitionService.getCompetitionById(1L);

    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getName()).isEqualTo("Bundesliga");
  }

  @Test
  void getCompetitionById_notFound_throwsException() {
    when(competitionRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> competitionService.getCompetitionById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");
  }

  @Test
  void createCompetition_savesAndReturns() {
    Competition newComp = new Competition(null, "Champions League");
    Competition savedComp = new Competition(3L, "Champions League");
    when(competitionRepo.save(any(Competition.class))).thenReturn(savedComp);

    Competition result = competitionService.createCompetition(newComp);

    assertThat(result.getId()).isEqualTo(3L);
    assertThat(result.getName()).isEqualTo("Champions League");
    verify(competitionRepo).save(newComp);
  }

  @Test
  void updateCompetition_updatesAndReturns() {
    Competition updated = new Competition(1L, "Austrian Bundesliga");
    when(competitionRepo.findById(1L)).thenReturn(Optional.of(bundesliga));
    when(competitionRepo.save(any(Competition.class))).thenReturn(updated);

    Competition result = competitionService.updateCompetition(1L, new Competition(null, "Austrian Bundesliga"));

    assertThat(result.getName()).isEqualTo("Austrian Bundesliga");
    verify(competitionRepo).save(bundesliga);
  }

  @Test
  void updateCompetition_notFound_throwsException() {
    when(competitionRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> competitionService.updateCompetition(99L, new Competition(null, "Test")))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(competitionRepo, never()).save(any());
  }

  @Test
  void deleteCompetition_deletes() {
    when(competitionRepo.existsById(1L)).thenReturn(true);

    competitionService.deleteCompetition(1L);

    verify(competitionRepo).deleteById(1L);
  }

  @Test
  void deleteCompetition_notFound_throwsException() {
    when(competitionRepo.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> competitionService.deleteCompetition(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(competitionRepo, never()).deleteById(any());
  }
}
