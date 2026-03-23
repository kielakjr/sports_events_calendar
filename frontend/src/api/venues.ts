import client from './client';
import type { Venue } from '../types/event';

export const fetchVenues = () =>
  client.get<Venue[]>('/venues').then((res) => res.data);

export const createVenue = (data: { name: string; city: string }) =>
  client.post<Venue>('/venues', data).then((res) => res.data);

export const deleteVenue = (id: number) =>
  client.delete(`/venues/${id}`);
