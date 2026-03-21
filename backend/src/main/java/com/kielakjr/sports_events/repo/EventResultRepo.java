package com.kielakjr.sports_events.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.EventResult;

@Repository
public interface EventResultRepo extends JpaRepository<EventResult, Long> {

  List<EventResult> findByEventId(Long eventId);

  @Query("SELECT er FROM EventResult er " +
         "JOIN FETCH er.team " +
         "WHERE er.event.id IN :eventIds")
  List<EventResult> findByEventIds(@Param("eventIds") List<Long> eventIds);

  void deleteByEventId(Long eventId);
}
