package com.kielakjr.sports_events.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class EventParticipant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nullable
  private String role;

  @ManyToOne
  @JoinColumn(name = "_event_id")
  @NotNull(message = "Event is mandatory")
  private Event event;

  @ManyToOne
  @JoinColumn(name = "_team_id")
  @NotNull(message = "Team is mandatory")
  private Team team;
}
