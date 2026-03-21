package com.kielakjr.sports_events.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_date")
  private LocalDate eventDate;

  @Column(name = "event_time")
  private LocalTime eventTime;
  private String season;
  private String status;
  private String stage;

  @ManyToOne
  @JoinColumn(name = "_competition_id")
  private Competition competition;

  @ManyToOne
  @JoinColumn(name = "_venue_id")
  private Venue venue;
}
