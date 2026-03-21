package com.kielakjr.sports_events.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.sql.Date;
import java.sql.Time;

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

  private Date event_date;
  private Time event_time;
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
