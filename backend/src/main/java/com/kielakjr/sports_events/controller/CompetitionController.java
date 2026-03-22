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

import com.kielakjr.sports_events.model.Competition;
import com.kielakjr.sports_events.service.CompetitionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

  @Autowired
  private CompetitionService competitionService;

  @GetMapping
  public ResponseEntity<List<Competition>> getAllCompetitions() {
    return ResponseEntity.ok(competitionService.getAllCompetitions());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Competition> getCompetitionById(@PathVariable Long id) {
    return ResponseEntity.ok(competitionService.getCompetitionById(id));
  }

  @PostMapping
  public ResponseEntity<Competition> createCompetition(@Valid @RequestBody Competition competition) {
    return ResponseEntity.status(HttpStatus.CREATED).body(competitionService.createCompetition(competition));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Competition> updateCompetition(@PathVariable Long id, @Valid @RequestBody Competition competition) {
    return ResponseEntity.ok(competitionService.updateCompetition(id, competition));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
    competitionService.deleteCompetition(id);
    return ResponseEntity.noContent().build();
  }
}
