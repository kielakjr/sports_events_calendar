package com.kielakjr.sports_events.service;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.kielakjr.sports_events.dto.EventRequestDTO;
import com.kielakjr.sports_events.dto.EventResponseDTO;
import com.kielakjr.sports_events.model.Competition;
import com.kielakjr.sports_events.model.Event;
import com.kielakjr.sports_events.model.EventParticipant;
import com.kielakjr.sports_events.model.EventResult;
import com.kielakjr.sports_events.model.EventStatus;
import com.kielakjr.sports_events.model.Sport;
import com.kielakjr.sports_events.model.Team;
import com.kielakjr.sports_events.model.Venue;
import com.kielakjr.sports_events.repo.CompetitionRepo;
import com.kielakjr.sports_events.repo.EventParticipantRepo;
import com.kielakjr.sports_events.repo.EventRepo;
import com.kielakjr.sports_events.repo.EventResultRepo;
import com.kielakjr.sports_events.repo.TeamRepo;
import com.kielakjr.sports_events.repo.VenueRepo;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

  @Mock private EventRepo eventRepo;
  @Mock private EventParticipantRepo participantRepo;
  @Mock private EventResultRepo resultRepo;
  @Mock private CompetitionRepo competitionRepo;
  @Mock private VenueRepo venueRepo;
  @Mock private TeamRepo teamRepo;

  @InjectMocks
  private EventService eventService;

  private Sport sport;
  private Competition competition;
  private Venue venue;
  private Team teamHome;
  private Team teamAway;
  private Event event;

  @BeforeEach
  void setUp() {
    sport = new Sport(1L, "Football");
    competition = new Competition(1L, "Bundesliga");
    venue = new Venue(1L, "Red Bull Arena", "Salzburg");
    teamHome = new Team(1L, "Salzburg", "FC Red Bull Salzburg", "SAL", "AT", sport);
    teamAway = new Team(2L, "Sturm", "SK Sturm Graz", "STU", "AT", sport);

    event = new Event();
    event.setId(1L);
    event.setEventDate(LocalDate.of(2019, 7, 18));
    event.setEventTime(LocalTime.of(18, 30));
    event.setSeason("2019/2020");
    event.setStatus(EventStatus.SCHEDULED);
    event.setStage("Regular Season");
    event.setCompetition(competition);
    event.setVenue(venue);
  }

  @Test
  void getAllEvents_returnsListOfDTOs() {
    EventParticipant p1 = new EventParticipant(1L, "HOME", event, teamHome);
    EventParticipant p2 = new EventParticipant(2L, "AWAY", event, teamAway);

    when(eventRepo.findAllWithDetails()).thenReturn(List.of(event));
    when(participantRepo.findByEventIds(List.of(1L))).thenReturn(List.of(p1, p2));
    when(resultRepo.findByEventIds(List.of(1L))).thenReturn(List.of());

    List<EventResponseDTO> result = eventService.getAllEvents(null, null, null, null, null);

    assertThat(result).hasSize(1);
    EventResponseDTO dto = result.get(0);
    assertThat(dto.getCompetitionName()).isEqualTo("Bundesliga");
    assertThat(dto.getVenueName()).isEqualTo("Red Bull Arena");
    assertThat(dto.getSportName()).isEqualTo("Football");
    assertThat(dto.getParticipants()).hasSize(2);
    assertThat(dto.getParticipants().get(0).getTeamName()).isEqualTo("Salzburg");
    assertThat(dto.getParticipants().get(1).getTeamName()).isEqualTo("Sturm");
  }

  @Test
  void getAllEvents_emptyList() {
    when(eventRepo.findAllWithDetails()).thenReturn(List.of());

    List<EventResponseDTO> result = eventService.getAllEvents(null, null, null, null, null);

    assertThat(result).isEmpty();
    verify(participantRepo, never()).findByEventIds(any());
    verify(resultRepo, never()).findByEventIds(any());
  }

  @Test
  void getEventById_returnsDTO() {
    EventParticipant p1 = new EventParticipant(1L, "HOME", event, teamHome);
    EventResult r1 = new EventResult(1L, 3, true, teamHome, event);

    when(eventRepo.findByIdWithDetails(1L)).thenReturn(Optional.of(event));
    when(participantRepo.findByEventId(1L)).thenReturn(List.of(p1));
    when(resultRepo.findByEventId(1L)).thenReturn(List.of(r1));

    EventResponseDTO dto = eventService.getEventById(1L);

    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getEventDate()).isEqualTo(LocalDate.of(2019, 7, 18));
    assertThat(dto.getEventTime()).isEqualTo(LocalTime.of(18, 30));
    assertThat(dto.getResults()).hasSize(1);
    assertThat(dto.getResults().get(0).getScore()).isEqualTo(3);
    assertThat(dto.getResults().get(0).isWin()).isTrue();
  }

  @Test
  void getEventById_notFound_throwsException() {
    when(eventRepo.findByIdWithDetails(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> eventService.getEventById(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");
  }

  @Test
  void createEvent_savesEventAndParticipants() {
    EventRequestDTO.ParticipantDTO pDto1 = new EventRequestDTO.ParticipantDTO();
    pDto1.setTeamId(1L);
    pDto1.setRole("HOME");
    EventRequestDTO.ParticipantDTO pDto2 = new EventRequestDTO.ParticipantDTO();
    pDto2.setTeamId(2L);
    pDto2.setRole("AWAY");

    EventRequestDTO requestDTO = new EventRequestDTO();
    requestDTO.setEventDate(LocalDate.of(2019, 7, 18));
    requestDTO.setEventTime(LocalTime.of(18, 30));
    requestDTO.setSeason("2019/2020");
    requestDTO.setStatus(EventStatus.SCHEDULED);
    requestDTO.setStage("Regular Season");
    requestDTO.setCompetitionId(1L);
    requestDTO.setVenueId(1L);
    requestDTO.setParticipants(List.of(pDto1, pDto2));

    when(competitionRepo.findById(1L)).thenReturn(Optional.of(competition));
    when(venueRepo.findById(1L)).thenReturn(Optional.of(venue));
    when(teamRepo.findById(1L)).thenReturn(Optional.of(teamHome));
    when(teamRepo.findById(2L)).thenReturn(Optional.of(teamAway));
    when(eventRepo.save(any(Event.class))).thenReturn(event);
    when(participantRepo.save(any(EventParticipant.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    when(eventRepo.findByIdWithDetails(1L)).thenReturn(Optional.of(event));
    EventParticipant savedP1 = new EventParticipant(1L, "HOME", event, teamHome);
    EventParticipant savedP2 = new EventParticipant(2L, "AWAY", event, teamAway);
    when(participantRepo.findByEventId(1L)).thenReturn(List.of(savedP1, savedP2));
    when(resultRepo.findByEventId(1L)).thenReturn(List.of());

    EventResponseDTO result = eventService.createEvent(requestDTO);

    assertThat(result.getCompetitionName()).isEqualTo("Bundesliga");
    assertThat(result.getParticipants()).hasSize(2);
    verify(eventRepo).save(any(Event.class));
    verify(participantRepo, org.mockito.Mockito.times(2)).save(any(EventParticipant.class));
  }

  @Test
  void createEvent_competitionNotFound_throwsException() {
    EventRequestDTO requestDTO = new EventRequestDTO();
    requestDTO.setCompetitionId(99L);
    requestDTO.setVenueId(1L);
    requestDTO.setParticipants(List.of());

    when(competitionRepo.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> eventService.createEvent(requestDTO))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Competition not found");
  }

  @Test
  void deleteEvent_deletesRelatedEntities() {
    when(eventRepo.existsById(1L)).thenReturn(true);

    eventService.deleteEvent(1L);

    verify(resultRepo).deleteByEventId(1L);
    verify(participantRepo).deleteByEventId(1L);
    verify(eventRepo).deleteById(1L);
  }

  @Test
  void deleteEvent_notFound_throwsException() {
    when(eventRepo.existsById(99L)).thenReturn(false);

    assertThatThrownBy(() -> eventService.deleteEvent(99L))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("99");

    verify(eventRepo, never()).deleteById(any());
  }
}
