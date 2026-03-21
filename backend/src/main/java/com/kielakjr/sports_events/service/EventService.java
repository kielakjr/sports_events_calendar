package com.kielakjr.sports_events.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kielakjr.sports_events.repo.EventRepo;
import com.kielakjr.sports_events.model.Event;

@Service
public class EventService {

  @Autowired
  private EventRepo eventRepo;

  public List<Event> getAllEvents() {
    return eventRepo.findAll();
  }
}
