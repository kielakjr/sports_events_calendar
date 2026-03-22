package com.kielakjr.sports_events.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name is mandatory")
  private String name;
  @NotBlank(message = "Official name is mandatory")
  private String officialName;
  @NotBlank(message = "Abbreviation is mandatory")
  private String abbreviation;
  @NotBlank(message = "Country code is mandatory")
  @Size(min = 3, max = 3, message = "Country code must be exactly 3 characters")
  private String teamCountryCode;

  @ManyToOne
  @JoinColumn(name = "_sport_id")
  @NotNull(message = "Sport is mandatory")
  private Sport sport;
}
