import { useEffect, useState } from 'react';
import type { EventRequest, EventStatus, Competition, Venue, Team } from '../types/event';
import { createEvent } from '../api/events';
import { fetchCompetitions } from '../api/competitions';
import { fetchVenues } from '../api/venues';
import { fetchTeams } from '../api/teams';

interface Props {
  onCreated: () => void;
  onCancel: () => void;
}

const STATUSES: EventStatus[] = ['SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELED'];

const EventForm = ({ onCreated, onCancel }: Props) => {
  const [competitions, setCompetitions] = useState<Competition[]>([]);
  const [venues, setVenues] = useState<Venue[]>([]);
  const [teams, setTeams] = useState<Team[]>([]);
  const [error, setError] = useState<string | null>(null);

  const [form, setForm] = useState<EventRequest>({
    eventDate: '',
    eventTime: '',
    season: '',
    status: 'SCHEDULED',
    stage: '',
    competitionId: 0,
    venueId: 0,
    participants: [
      { teamId: 0, role: 'HOME' },
      { teamId: 0, role: 'AWAY' },
    ],
  });

  useEffect(() => {
    Promise.all([fetchCompetitions(), fetchVenues(), fetchTeams()])
      .then(([c, v, t]) => {
        setCompetitions(c);
        setVenues(v);
        setTeams(t);
      })
      .catch((err) => setError(err.message));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      await createEvent(form);
      onCreated();
    } catch (err: unknown) {
      if (typeof err === 'object' && err !== null && 'response' in err) {
        const axiosErr = err as { response?: { data?: unknown } };
        const data = axiosErr.response?.data;
        if (typeof data === 'object' && data !== null) {
          setError(Object.entries(data).map(([k, v]) => `${k}: ${v}`).join(', '));
        } else if (typeof data === 'string') {
          setError(data);
        } else {
          setError('Failed to create event');
        }
      } else {
        setError(err instanceof Error ? err.message : 'Failed to create event');
      }
    }
  };

  const updateParticipant = (index: number, teamId: number) => {
    setForm((prev) => {
      const participants = [...prev.participants];
      participants[index] = { ...participants[index], teamId };
      return { ...prev, participants };
    });
  };

  return (
    <div>
      <div className="section-header">
        <h1>New Event</h1>
      </div>
      <form className="form" onSubmit={handleSubmit}>
        {error && <p className="error">{error}</p>}

        <div className="form-row">
          <label>
            Date
            <input type="date" required value={form.eventDate}
              onChange={(e) => setForm({ ...form, eventDate: e.target.value })} />
          </label>
          <label>
            Time
            <input type="time" required step="1" value={form.eventTime}
              onChange={(e) => setForm({ ...form, eventTime: e.target.value })} />
          </label>
        </div>

        <div className="form-row">
          <label>
            Season
            <input type="text" required placeholder="e.g. 2024/2025" value={form.season}
              onChange={(e) => setForm({ ...form, season: e.target.value })} />
          </label>
          <label>
            Stage
            <input type="text" required placeholder="e.g. Regular Season" value={form.stage}
              onChange={(e) => setForm({ ...form, stage: e.target.value })} />
          </label>
        </div>

        <div className="form-row">
          <label>
            Status
            <select value={form.status}
              onChange={(e) => setForm({ ...form, status: e.target.value as EventStatus })}>
              {STATUSES.map((s) => <option key={s} value={s}>{s}</option>)}
            </select>
          </label>
          <label>
            Competition
            <select required value={form.competitionId}
              onChange={(e) => setForm({ ...form, competitionId: Number(e.target.value) })}>
              <option value={0} disabled>Select...</option>
              {competitions.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>
          </label>
        </div>

        <label>
          Venue
          <select required value={form.venueId}
            onChange={(e) => setForm({ ...form, venueId: Number(e.target.value) })}>
            <option value={0} disabled>Select...</option>
            {venues.map((v) => <option key={v.id} value={v.id}>{v.name} - {v.city}</option>)}
          </select>
        </label>

        <div className="form-row">
          <label>
            Home Team
            <select required value={form.participants[0].teamId}
              onChange={(e) => updateParticipant(0, Number(e.target.value))}>
              <option value={0} disabled>Select...</option>
              {teams.map((t) => <option key={t.id} value={t.id}>{t.name}</option>)}
            </select>
          </label>
          <label>
            Away Team
            <select required value={form.participants[1].teamId}
              onChange={(e) => updateParticipant(1, Number(e.target.value))}>
              <option value={0} disabled>Select...</option>
              {teams.map((t) => <option key={t.id} value={t.id}>{t.name}</option>)}
            </select>
          </label>
        </div>

        <div className="form-actions">
          <button type="button" className="btn" onClick={onCancel}>Cancel</button>
          <button type="submit" className="btn btn-primary">Create Event</button>
        </div>
      </form>
    </div>
  );
};

export default EventForm;
