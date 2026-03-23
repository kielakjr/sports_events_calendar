package com.kielakjr.sports_events.service;

import com.kielakjr.sports_events.repo.TeamRepo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kielakjr.sports_events.model.Sport;
import com.kielakjr.sports_events.repo.SportRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SportService {
  @Autowired
  private SportRepo sportRepo;
  @Autowired
  private TeamRepo teamRepo;

  public List<Sport> getAllSports() {
    return sportRepo.findAll();
  }

  public Sport getSportById(Long id) {
    return sportRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Sport not found with id: " + id));
  }

  public Sport createSport(Sport sport) {
    return sportRepo.save(sport);
  }

  public Sport updateSport(Long id, Sport sportDetails) {
    Sport sport = sportRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Sport not found with id: " + id));
    sport.setName(sportDetails.getName());
    return sportRepo.save(sport);
  }

  public void deleteSport(Long id) {
    if (!sportRepo.existsById(id)) {
      throw new EntityNotFoundException("Sport not found with id: " + id);
    }
    if (!teamRepo.findBySportId(id).isEmpty()) {
      throw new IllegalStateException("Cannot delete sport with associated teams");
    }
    sportRepo.deleteById(id);
  }
}
