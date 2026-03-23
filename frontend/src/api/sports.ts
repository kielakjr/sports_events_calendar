import client from './client';
import type { Sport } from '../types/event';

export const fetchSports = () =>
  client.get<Sport[]>('/sports').then((res) => res.data);

export const createSport = (data: { name: string }) =>
  client.post<Sport>('/sports', data).then((res) => res.data);

export const deleteSport = (id: number) =>
  client.delete(`/sports/${id}`);
