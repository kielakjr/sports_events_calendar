import client from './client';
import type { SportEvent, EventRequest } from '../types/event';

export const fetchEvents = () =>
  client.get<SportEvent[]>('/events').then((res) => res.data);

export const fetchEvent = (id: number) =>
  client.get<SportEvent>(`/events/${id}`).then((res) => res.data);

export const createEvent = (data: EventRequest) =>
  client.post<SportEvent>('/events', data).then((res) => res.data);

export const updateEvent = (id: number, data: EventRequest) =>
  client.put<SportEvent>(`/events/${id}`, data).then((res) => res.data);

export const deleteEvent = (id: number) =>
  client.delete(`/events/${id}`);
