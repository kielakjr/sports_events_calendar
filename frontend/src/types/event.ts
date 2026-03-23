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

export interface EventRequest {
  eventDate: string;
  eventTime: string;
  season: string;
  status: EventStatus;
  stage: string;
  competitionId: number;
  venueId: number;
  participants: { teamId: number; role: string }[];
}

export interface Sport {
  id: number;
  name: string;
}

export interface Team {
  id: number;
  name: string;
  officialName: string;
  abbreviation: string;
  teamCountryCode: string;
  sport: Sport;
}

export interface Venue {
  id: number;
  name: string;
  city: string;
}

export interface Competition {
  id: number;
  name: string;
}
