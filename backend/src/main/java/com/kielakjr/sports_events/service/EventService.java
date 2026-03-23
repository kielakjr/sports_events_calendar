package com.kielakjr.sports_events.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kielakjr.sports_events.dto.EventRequestDTO;
import com.kielakjr.sports_events.dto.EventResponseDTO;
import com.kielakjr.sports_events.model.Competition;
import com.kielakjr.sports_events.model.Event;
import com.kielakjr.sports_events.model.EventParticipant;
import com.kielakjr.sports_events.model.EventResult;
import com.kielakjr.sports_events.model.Team;
import com.kielakjr.sports_events.model.Venue;
import com.kielakjr.sports_events.repo.CompetitionRepo;
import com.kielakjr.sports_events.repo.EventParticipantRepo;
import com.kielakjr.sports_events.repo.EventRepo;
import com.kielakjr.sports_events.repo.EventResultRepo;
import com.kielakjr.sports_events.repo.TeamRepo;
import com.kielakjr.sports_events.repo.VenueRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {

  @Autowired
  private EventRepo eventRepo;
  @Autowired
  private EventParticipantRepo participantRepo;
  @Autowired
  private EventResultRepo resultRepo;
  @Autowired
  private CompetitionRepo competitionRepo;
  @Autowired
  private VenueRepo venueRepo;
  @Autowired
  private TeamRepo teamRepo;

  public List<EventResponseDTO> getAllEvents(String sportName, LocalDate date,
                                              Long venueId, Long teamId, Long competitionId) {
    boolean hasFilters = sportName != null || date != null
        || venueId != null || teamId != null || competitionId != null;
    List<Event> events = hasFilters
        ? eventRepo.findWithFilters(sportName, date, venueId, teamId, competitionId)
        : eventRepo.findAllWithDetails();
    List<Long> eventIds = events.stream().map(Event::getId).toList();

    List<EventParticipant> allParticipants = eventIds.isEmpty()
        ? List.of()
        : participantRepo.findByEventIds(eventIds);
    List<EventResult> allResults = eventIds.isEmpty()
        ? List.of()
        : resultRepo.findByEventIds(eventIds);

    return events.stream()
        .map(event -> toResponseDTO(event,
            allParticipants.stream()
                .filter(p -> p.getEvent().getId().equals(event.getId()))
                .toList(),
            allResults.stream()
                .filter(r -> r.getEvent().getId().equals(event.getId()))
                .toList()))
        .collect(Collectors.toList());
  }

  public EventResponseDTO getEventById(Long id) {
    Event event = eventRepo.findByIdWithDetails(id)
        .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

    List<EventParticipant> participants = participantRepo.findByEventId(id);
    List<EventResult> results = resultRepo.findByEventId(id);

    return toResponseDTO(event, participants, results);
  }

  @Transactional
  public EventResponseDTO createEvent(EventRequestDTO dto) {
    Competition competition = competitionRepo.findById(dto.getCompetitionId())
        .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + dto.getCompetitionId()));
    Venue venue = venueRepo.findById(dto.getVenueId())
        .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + dto.getVenueId()));

    Event event = new Event();
    event.setEventDate(dto.getEventDate());
    event.setEventTime(dto.getEventTime());
    event.setSeason(dto.getSeason());
    event.setStatus(dto.getStatus());
    event.setStage(dto.getStage());
    event.setCompetition(competition);
    event.setVenue(venue);
    event = eventRepo.save(event);

    for (EventRequestDTO.ParticipantDTO pDto : dto.getParticipants()) {
      Team team = teamRepo.findById(pDto.getTeamId())
          .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + pDto.getTeamId()));

      EventParticipant participant = new EventParticipant();
      participant.setEvent(event);
      participant.setTeam(team);
      participant.setRole(pDto.getRole());
      participantRepo.save(participant);
    }

    return getEventById(event.getId());
  }

  @Transactional
  public EventResponseDTO updateEvent(Long id, EventRequestDTO dto) {
    Event event = eventRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

    Competition competition = competitionRepo.findById(dto.getCompetitionId())
        .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + dto.getCompetitionId()));
    Venue venue = venueRepo.findById(dto.getVenueId())
        .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + dto.getVenueId()));

    event.setEventDate(dto.getEventDate());
    event.setEventTime(dto.getEventTime());
    event.setSeason(dto.getSeason());
    event.setStatus(dto.getStatus());
    event.setStage(dto.getStage());
    event.setCompetition(competition);
    event.setVenue(venue);
    eventRepo.save(event);

    participantRepo.deleteByEventId(id);
    for (EventRequestDTO.ParticipantDTO pDto : dto.getParticipants()) {
      Team team = teamRepo.findById(pDto.getTeamId())
          .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + pDto.getTeamId()));

      EventParticipant participant = new EventParticipant();
      participant.setEvent(event);
      participant.setTeam(team);
      participant.setRole(pDto.getRole());
      participantRepo.save(participant);
    }

    return getEventById(id);
  }

  @Transactional
  public void deleteEvent(Long id) {
    if (!eventRepo.existsById(id)) {
      throw new EntityNotFoundException("Event not found with id: " + id);
    }
    resultRepo.deleteByEventId(id);
    participantRepo.deleteByEventId(id);
    eventRepo.deleteById(id);
  }

  private EventResponseDTO toResponseDTO(Event event,
                                          List<EventParticipant> participants,
                                          List<EventResult> results) {
    String sportName = participants.stream()
        .filter(p -> p.getTeam().getSport() != null)
        .map(p -> p.getTeam().getSport().getName())
        .findFirst()
        .orElse(null);

    List<EventResponseDTO.ParticipantInfo> participantInfos = participants.stream()
        .map(p -> EventResponseDTO.ParticipantInfo.builder()
            .teamId(p.getTeam().getId())
            .teamName(p.getTeam().getName())
            .abbreviation(p.getTeam().getAbbreviation())
            .role(p.getRole())
            .build())
        .toList();

    List<EventResponseDTO.ResultInfo> resultInfos = results.stream()
        .map(r -> EventResponseDTO.ResultInfo.builder()
            .teamId(r.getTeam().getId())
            .teamName(r.getTeam().getName())
            .score(r.getScore())
            .win(r.isWin())
            .build())
        .toList();

    return EventResponseDTO.builder()
        .id(event.getId())
        .eventDate(event.getEventDate())
        .eventTime(event.getEventTime())
        .season(event.getSeason())
        .status(event.getStatus())
        .stage(event.getStage())
        .competitionName(event.getCompetition().getName())
        .venueName(event.getVenue().getName())
        .venueCity(event.getVenue().getCity())
        .sportName(sportName)
        .participants(participantInfos)
        .results(resultInfos)
        .build();
  }
}
