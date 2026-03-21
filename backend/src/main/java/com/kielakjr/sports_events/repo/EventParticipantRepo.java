package com.kielakjr.sports_events.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.EventParticipant;

@Repository
public interface EventParticipantRepo extends JpaRepository<EventParticipant, Long> {

  List<EventParticipant> findByEventId(Long eventId);

  @Query("SELECT ep FROM EventParticipant ep " +
         "JOIN FETCH ep.team t " +
         "LEFT JOIN FETCH t.sport " +
         "WHERE ep.event.id IN :eventIds")
  List<EventParticipant> findByEventIds(@Param("eventIds") List<Long> eventIds);

  void deleteByEventId(Long eventId);
}
