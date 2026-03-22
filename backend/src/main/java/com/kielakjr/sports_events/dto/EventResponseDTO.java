package com.kielakjr.sports_events.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.kielakjr.sports_events.model.EventStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventResponseDTO {

  private Long id;
  private LocalDate eventDate;
  private LocalTime eventTime;
  private String season;
  private EventStatus status;
  private String stage;
  private String competitionName;
  private String venueName;
  private String venueCity;
  private String sportName;

  private List<ParticipantInfo> participants;
  private List<ResultInfo> results;

  @Data
  @Builder
  public static class ParticipantInfo {
    private Long teamId;
    private String teamName;
    private String abbreviation;
    private String role;
  }

  @Data
  @Builder
  public static class ResultInfo {
    private Long teamId;
    private String teamName;
    private int score;
    private boolean win;
  }
}
