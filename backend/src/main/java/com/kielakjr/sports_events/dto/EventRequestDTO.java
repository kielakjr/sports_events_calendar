package com.kielakjr.sports_events.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.kielakjr.sports_events.model.EventStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventRequestDTO {

  @NotNull
  private LocalDate eventDate;

  @NotNull
  private LocalTime eventTime;

  @NotBlank(message = "Season is mandatory")
  private String season;

  @NotNull(message = "Status is mandatory")
  private EventStatus status;

  @NotBlank(message = "Stage is mandatory")
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
