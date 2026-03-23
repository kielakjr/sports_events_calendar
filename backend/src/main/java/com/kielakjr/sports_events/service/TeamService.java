package com.kielakjr.sports_events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kielakjr.sports_events.repo.SportRepo;
import com.kielakjr.sports_events.repo.TeamRepo;
import com.kielakjr.sports_events.repo.EventRepo;
import com.kielakjr.sports_events.model.Sport;
import com.kielakjr.sports_events.model.Team;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class TeamService {

  @Autowired
  private TeamRepo teamRepo;

  @Autowired
  private SportRepo sportRepo;

  @Autowired
  private EventRepo eventRepo;

  public List<Team> getAllTeams() {
    return teamRepo.findAll();
  }

  public Team getTeamById(Long id) {
    return teamRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
  }

  public Team createTeam(Team team) {
    if (team.getSport() != null && team.getSport().getId() != null) {
      Sport sport = sportRepo.findById(team.getSport().getId())
          .orElseThrow(() -> new EntityNotFoundException("Sport not found with id: " + team.getSport().getId()));
      team.setSport(sport);
    }
    return teamRepo.save(team);
  }

  public Team updateTeam(Long id, Team teamDetails) {
    Team team = teamRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
    team.setName(teamDetails.getName());
    team.setOfficialName(teamDetails.getOfficialName());
    team.setAbbreviation(teamDetails.getAbbreviation());
    team.setTeamCountryCode(teamDetails.getTeamCountryCode());
    if (teamDetails.getSport() != null && teamDetails.getSport().getId() != null) {
      Sport sport = sportRepo.findById(teamDetails.getSport().getId())
          .orElseThrow(() -> new EntityNotFoundException("Sport not found with id: " + teamDetails.getSport().getId()));
      team.setSport(sport);
    }
    return teamRepo.save(team);
  }

  public void deleteTeam(Long id) {
    if (!teamRepo.existsById(id)) {
      throw new EntityNotFoundException("Team not found with id: " + id);
    }
    if (!eventRepo.findByTeamId(id).isEmpty()) {
      throw new IllegalStateException("Cannot delete team with id: " + id + " because it is associated with existing events");
    }
    teamRepo.deleteById(id);
  }
}
