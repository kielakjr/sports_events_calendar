package com.kielakjr.sports_events.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.Event;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

  @Query("SELECT e FROM Event e " +
         "JOIN FETCH e.competition " +
         "JOIN FETCH e.venue " +
         "ORDER BY e.eventDate DESC, e.eventTime DESC")
  List<Event> findAllWithDetails();

  @Query("SELECT e FROM Event e " +
         "JOIN FETCH e.competition " +
         "JOIN FETCH e.venue " +
         "WHERE e.id = :id")
  Optional<Event> findByIdWithDetails(@Param("id") Long id);

  @Query("SELECT e FROM Event e " +
         "JOIN FETCH e.competition " +
         "JOIN FETCH e.venue " +
         "WHERE e.id IN (SELECT ep.event.id FROM EventParticipant ep WHERE ep.team.sport.name = :sport) " +
         "ORDER BY e.eventDate DESC, e.eventTime DESC")
  List<Event> findBySport(@Param("sport") String sport);

  @Query("SELECT e FROM Event e " +
         "JOIN FETCH e.competition " +
         "JOIN FETCH e.venue " +
         "WHERE e.id IN (SELECT ep.event.id FROM EventParticipant ep WHERE ep.team.id = :teamId) " +
         "ORDER BY e.eventDate DESC, e.eventTime DESC")
  List<Event> findByTeamId(@Param("teamId") Long teamId);

  @Query("SELECT e FROM Event e " +
         "JOIN FETCH e.competition " +
         "JOIN FETCH e.venue " +
         "WHERE e.competition.id = :competitionId " +
         "ORDER BY e.eventDate DESC, e.eventTime DESC")
  List<Event> findByCompetitionId(@Param("competitionId") Long competitionId);

  @Query("SELECT e FROM Event e " +
         "JOIN FETCH e.competition " +
         "JOIN FETCH e.venue " +
         "WHERE e.venue.id = :venueId " +
         "ORDER BY e.eventDate DESC, e.eventTime DESC")
  List<Event> findByVenueId(@Param("venueId") Long venueId);
}
