package com.kielakjr.sports_events.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.Venue;

@Repository
public interface VenueRepo extends JpaRepository<Venue, Long> {
  Optional<Venue> findByName(String name);
}
