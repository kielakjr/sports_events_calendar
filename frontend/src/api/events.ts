import client from './client';
import type { SportEvent, EventRequest } from '../types/event';

export const fetchEvents = (filters?: {
  sport?: string;
  date?: string;
  venueId?: string;
  teamId?: string;
  competitionId?: string;
}) => {
  const params = new URLSearchParams();
  if (filters?.sport) params.set('sport', filters.sport);
  if (filters?.date) params.set('date', filters.date);
  if (filters?.venueId) params.set('venueId', filters.venueId);
  if (filters?.teamId) params.set('teamId', filters.teamId);
  if (filters?.competitionId) params.set('competitionId', filters.competitionId);
  const query = params.toString();
  return client.get<SportEvent[]>(`/events${query ? `?${query}` : ''}`).then((res) => res.data);
};

export const fetchEvent = (id: number) =>
  client.get<SportEvent>(`/events/${id}`).then((res) => res.data);

export const createEvent = (data: EventRequest) =>
  client.post<SportEvent>('/events', data).then((res) => res.data);

export const updateEvent = (id: number, data: EventRequest) =>
  client.put<SportEvent>(`/events/${id}`, data).then((res) => res.data);

export const deleteEvent = (id: number) =>
  client.delete(`/events/${id}`);
