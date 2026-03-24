package com.kielakjr.sports_events.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.Competition;

@Repository
public interface CompetitionRepo extends JpaRepository<Competition, Long> {
  Optional<Competition> findByName(String name);
}
