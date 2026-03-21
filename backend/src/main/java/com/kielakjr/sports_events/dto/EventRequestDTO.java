package com.kielakjr.sports_events.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventRequestDTO {

  @NotNull
  private LocalDate eventDate;

  @NotNull
  private LocalTime eventTime;

  private String season;
  private String status;
  private String stage;

  @NotNull
  private Long competitionId;

  @NotNull
  private Long venueId;

  @NotNull
  private List<ParticipantDTO> participants;

  @Data
  public static class ParticipantDTO {
    @NotNull
    private Long teamId;
    private String role;
  }
}
