package com.kielakjr.sports_events.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.Team;

@Repository
public interface TeamRepo extends JpaRepository<Team, Long> {

  List<Team> findBySportId(Long sportId);
  Optional<Team> findByName(String name);
}
