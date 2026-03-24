package com.kielakjr.sports_events.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kielakjr.sports_events.model.Sport;

@Repository
public interface SportRepo extends JpaRepository<Sport, Long> {
  Optional<Sport> findByName(String name);
}
