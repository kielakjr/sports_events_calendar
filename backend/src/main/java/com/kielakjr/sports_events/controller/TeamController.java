package com.kielakjr.sports_events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import com.kielakjr.sports_events.service.TeamService;
import com.kielakjr.sports_events.model.Team;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

  @Autowired
  private TeamService teamService;

  @GetMapping
  public List<Team> getAllTeams() {
    return teamService.getAllTeams();
  }

  @PostMapping
  public Team createTeam(@Valid @RequestBody Team team) {
    return teamService.createTeam(team);

  }

  @GetMapping("/{id}")
  public Team getTeamById(@PathVariable Long id) {
    return teamService.getTeamById(id);
  }
}
