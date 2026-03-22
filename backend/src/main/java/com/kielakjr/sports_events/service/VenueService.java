package com.kielakjr.sports_events.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kielakjr.sports_events.model.Venue;
import com.kielakjr.sports_events.repo.VenueRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VenueService {

  @Autowired
  private VenueRepo venueRepo;

  public List<Venue> getAllVenues() {
    return venueRepo.findAll();
  }

  public Venue getVenueById(Long id) {
    return venueRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + id));
  }

  public Venue createVenue(Venue venue) {
    return venueRepo.save(venue);
  }

  public Venue updateVenue(Long id, Venue venueDetails) {
    Venue venue = venueRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + id));
    venue.setName(venueDetails.getName());
    venue.setCity(venueDetails.getCity());
    return venueRepo.save(venue);
  }

  public void deleteVenue(Long id) {
    if (!venueRepo.existsById(id)) {
      throw new EntityNotFoundException("Venue not found with id: " + id);
    }
    venueRepo.deleteById(id);
  }
}
