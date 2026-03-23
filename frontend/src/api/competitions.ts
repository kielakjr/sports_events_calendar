import client from './client';
import type { Competition } from '../types/event';

export const fetchCompetitions = () =>
  client.get<Competition[]>('/competitions').then((res) => res.data);

export const createCompetition = (data: { name: string }) =>
  client.post<Competition>('/competitions', data).then((res) => res.data);

export const deleteCompetition = (id: number) =>
  client.delete(`/competitions/${id}`);
