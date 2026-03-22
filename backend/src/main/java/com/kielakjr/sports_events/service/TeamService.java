package com.kielakjr.sports_events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kielakjr.sports_events.repo.TeamRepo;
import com.kielakjr.sports_events.model.Team;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class TeamService {

  @Autowired
  private TeamRepo teamRepo;

  public List<Team> getAllTeams() {
    return teamRepo.findAll();
  }

  public Team getTeamById(Long id) {
    return teamRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
  }

  public Team createTeam(Team team) {
    return teamRepo.save(team);
  }

}
