package com.kielakjr.sports_events.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.Team;

@Repository
public interface TeamRepo extends JpaRepository<Team, Long> {
}
