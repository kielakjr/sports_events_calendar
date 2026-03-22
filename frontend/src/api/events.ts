import type { SportEvent } from '../types/event';

const BASE = '/api/events';

export async function fetchEvents(): Promise<SportEvent[]> {
  const res = await fetch(BASE);
  if (!res.ok) throw new Error('Failed to fetch events');
  return res.json();
}

export async function fetchEvent(id: number): Promise<SportEvent> {
  const res = await fetch(`${BASE}/${id}`);
  if (!res.ok) throw new Error('Failed to fetch event');
  return res.json();
}
