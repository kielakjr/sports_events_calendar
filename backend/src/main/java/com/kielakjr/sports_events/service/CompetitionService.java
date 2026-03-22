package com.kielakjr.sports_events.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kielakjr.sports_events.model.Competition;
import com.kielakjr.sports_events.repo.CompetitionRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CompetitionService {

  @Autowired
  private CompetitionRepo competitionRepo;

  public List<Competition> getAllCompetitions() {
    return competitionRepo.findAll();
  }

  public Competition getCompetitionById(Long id) {
    return competitionRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + id));
  }

  public Competition createCompetition(Competition competition) {
    return competitionRepo.save(competition);
  }

  public Competition updateCompetition(Long id, Competition competitionDetails) {
    Competition competition = competitionRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + id));
    competition.setName(competitionDetails.getName());
    return competitionRepo.save(competition);
  }

  public void deleteCompetition(Long id) {
    if (!competitionRepo.existsById(id)) {
      throw new EntityNotFoundException("Competition not found with id: " + id);
    }
    competitionRepo.deleteById(id);
  }
}
