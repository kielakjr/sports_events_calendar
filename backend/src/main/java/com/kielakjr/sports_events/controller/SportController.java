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

import com.kielakjr.sports_events.model.Sport;
import com.kielakjr.sports_events.service.SportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sports")
public class SportController {

  @Autowired
  private SportService sportService;

  @GetMapping
  public ResponseEntity<List<Sport>> getAllSports() {
    return ResponseEntity.ok(sportService.getAllSports());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Sport> getSportById(@PathVariable Long id) {
    return ResponseEntity.ok(sportService.getSportById(id));
  }

  @PostMapping
  public ResponseEntity<Sport> createSport(@Valid @RequestBody Sport sport) {
    return ResponseEntity.status(HttpStatus.CREATED).body(sportService.createSport(sport));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Sport> updateSport(@PathVariable Long id, @Valid @RequestBody Sport sport) {
    return ResponseEntity.ok(sportService.updateSport(id, sport));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
    sportService.deleteSport(id);
    return ResponseEntity.noContent().build();
  }
}
