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

import com.kielakjr.sports_events.model.Venue;
import com.kielakjr.sports_events.service.VenueService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

  @Autowired
  private VenueService venueService;

  @GetMapping
  public ResponseEntity<List<Venue>> getAllVenues() {
    return ResponseEntity.ok(venueService.getAllVenues());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
    return ResponseEntity.ok(venueService.getVenueById(id));
  }

  @PostMapping
  public ResponseEntity<Venue> createVenue(@Valid @RequestBody Venue venue) {
    return ResponseEntity.status(HttpStatus.CREATED).body(venueService.createVenue(venue));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @Valid @RequestBody Venue venue) {
    return ResponseEntity.ok(venueService.updateVenue(id, venue));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
    venueService.deleteVenue(id);
    return ResponseEntity.noContent().build();
  }
}
