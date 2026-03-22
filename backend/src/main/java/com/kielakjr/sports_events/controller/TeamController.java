package com.kielakjr.sports_events.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kielakjr.sports_events.model.Team;
import com.kielakjr.sports_events.service.TeamService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

  @Autowired
  private TeamService teamService;

  @GetMapping
  public ResponseEntity<List<Team>> getAllTeams() {
    return ResponseEntity.ok(teamService.getAllTeams());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
    return ResponseEntity.ok(teamService.getTeamById(id));
  }

  @PostMapping
  public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team) {
    return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(team));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Team> updateTeam(@PathVariable Long id, @Valid @RequestBody Team team) {
    return ResponseEntity.ok(teamService.updateTeam(id, team));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
    teamService.deleteTeam(id);
    return ResponseEntity.noContent().build();
  }
}
