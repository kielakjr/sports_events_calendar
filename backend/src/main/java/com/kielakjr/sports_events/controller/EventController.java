package com.kielakjr.sports_events.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import com.kielakjr.sports_events.model.Event;
import com.kielakjr.sports_events.service.EventService;


@RestController
public class EventController {

  @Autowired
  private EventService eventService;

  @GetMapping("/events")
  public ResponseEntity<List<Event>> getAllEvents() {
      return ResponseEntity.ok().body(eventService.getAllEvents());
  }

}
