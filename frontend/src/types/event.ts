export type EventStatus = 'SCHEDULED' | 'ONGOING' | 'COMPLETED' | 'CANCELED';

export interface ParticipantInfo {
  teamId: number;
  teamName: string;
  abbreviation: string;
  role: string | null;
}

export interface ResultInfo {
  teamId: number;
  teamName: string;
  score: number;
  win: boolean;
}

export interface SportEvent {
  id: number;
  eventDate: string;
  eventTime: string;
  season: string;
  status: EventStatus;
  stage: string;
  competitionName: string;
  venueName: string;
  venueCity: string;
  sportName: string;
  participants: ParticipantInfo[];
  results: ResultInfo[];
}
