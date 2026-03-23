import client from './client';
import type { Team } from '../types/event';

interface TeamRequest {
  name: string;
  officialName: string;
  abbreviation: string;
  teamCountryCode: string;
  sport: { id: number };
}

export const fetchTeams = () =>
  client.get<Team[]>('/teams').then((res) => res.data);

export const createTeam = (data: TeamRequest) =>
  client.post<Team>('/teams', data).then((res) => res.data);

export const deleteTeam = (id: number) =>
  client.delete(`/teams/${id}`);
