package com.kielakjr.sports_events.model;

import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.GenerationType;
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
public class EventResult {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Score is mandatory")
  private int score;
  @NotNull(message = "isWin is mandatory")
  private boolean isWin;

  @ManyToOne
  @JoinColumn(name = "_team_id")
  @NotNull(message = "Team is mandatory")
  private Team team;

  @ManyToOne
  @JoinColumn(name = "_event_id")
  @NotNull(message = "Event is mandatory")
  private Event event;
}
