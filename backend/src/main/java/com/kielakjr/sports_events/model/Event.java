package com.kielakjr.sports_events.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
  @NotNull(message = "Event date is mandatory")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate eventDate;

  @Column(name = "event_time")
  @NotNull(message = "Event time is mandatory")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime eventTime;
  @NotBlank(message = "Season is mandatory")
  private String season;
  @Enumerated(EnumType.STRING)
  @NotNull(message = "Status is mandatory")
  private EventStatus status;
  @NotBlank(message = "Stage is mandatory")
  private String stage;

  @ManyToOne
  @JoinColumn(name = "_competition_id")
  @NotNull(message = "Competition is mandatory")
  private Competition competition;

  @ManyToOne
  @JoinColumn(name = "_venue_id")
  @NotNull(message = "Venue is mandatory")
  private Venue venue;
}
