package com.kielakjr.sports_events.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.Event;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {

}
