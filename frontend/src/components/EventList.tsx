import { useEffect, useState } from 'react';
import type { SportEvent, Sport, Team, Venue, Competition } from '../types/event';
import { fetchEvents, deleteEvent } from '../api/events';
import { fetchSports } from '../api/sports';
import { fetchTeams } from '../api/teams';
import { fetchVenues } from '../api/venues';
import { fetchCompetitions } from '../api/competitions';

interface Props {
  onAdd: () => void;
  refreshKey: number;
}

const EventList = ({ onAdd, refreshKey }: Props) => {
  const [events, setEvents] = useState<SportEvent[]>([]);
  const [sports, setSports] = useState<Sport[]>([]);
  const [teams, setTeams] = useState<Team[]>([]);
  const [venues, setVenues] = useState<Venue[]>([]);
  const [competitions, setCompetitions] = useState<Competition[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [sportFilter, setSportFilter] = useState('');
  const [dateFilter, setDateFilter] = useState('');
  const [venueFilter, setVenueFilter] = useState('');
  const [teamFilter, setTeamFilter] = useState('');
  const [competitionFilter, setCompetitionFilter] = useState('');

  useEffect(() => {
    Promise.all([fetchSports(), fetchTeams(), fetchVenues(), fetchCompetitions()])
      .then(([s, t, v, c]) => { setSports(s); setTeams(t); setVenues(v); setCompetitions(c); })
      .catch(() => {});
  }, []);

  const load = () => {
    setLoading(true);
    setError(null);
    const filters: Record<string, string> = {};
    if (sportFilter) filters.sport = sportFilter;
    if (dateFilter) filters.date = dateFilter;
    if (venueFilter) filters.venueId = venueFilter;
    if (teamFilter) filters.teamId = teamFilter;
    if (competitionFilter) filters.competitionId = competitionFilter;
    fetchEvents(filters)
      .then(setEvents)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, [refreshKey, sportFilter, dateFilter, venueFilter, teamFilter, competitionFilter]);

  const handleDelete = async (id: number) => {
    try {
      await deleteEvent(id);
      setEvents((prev) => prev.filter((e) => e.id !== id));
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Delete failed';
      setError(msg);
    }
  };

  const clearFilters = () => {
    setSportFilter('');
    setDateFilter('');
    setVenueFilter('');
    setTeamFilter('');
    setCompetitionFilter('');
  };

  const hasFilters = sportFilter || dateFilter || venueFilter || teamFilter || competitionFilter;

  return (
    <div>
      <div className="section-header">
        <h1>Events</h1>
        <button className="btn btn-primary" onClick={onAdd}>+ New Event</button>
      </div>

      <div className="event-filters">
        <select value={sportFilter} onChange={(e) => setSportFilter(e.target.value)}>
          <option value="">All Sports</option>
          {sports.map((s) => (
            <option key={s.id} value={s.name}>{s.name}</option>
          ))}
        </select>
        <select value={competitionFilter} onChange={(e) => setCompetitionFilter(e.target.value)}>
          <option value="">All Competitions</option>
          {competitions.map((c) => (
            <option key={c.id} value={c.id}>{c.name}</option>
          ))}
        </select>
        <select value={venueFilter} onChange={(e) => setVenueFilter(e.target.value)}>
          <option value="">All Venues</option>
          {venues.map((v) => (
            <option key={v.id} value={v.id}>{v.name}</option>
          ))}
        </select>
        <select value={teamFilter} onChange={(e) => setTeamFilter(e.target.value)}>
          <option value="">All Teams</option>
          {teams.map((t) => (
            <option key={t.id} value={t.id}>{t.name}</option>
          ))}
        </select>
        <input
          type="date"
          value={dateFilter}
          onChange={(e) => setDateFilter(e.target.value)}
        />
        {hasFilters && (
          <button className="btn btn-sm" onClick={clearFilters}>Clear</button>
        )}
      </div>

      {loading ? (
        <p>Loading events...</p>
      ) : error ? (
        <p className="error">Error: {error}</p>
      ) : events.length === 0 ? (
        <p>No events found.</p>
      ) : (
        <div className="event-list">
          {events.map((event) => (
            <div key={event.id} className="event-card">
              <div className="event-header">
                <span className="event-sport">{event.sportName}</span>
                <span className={`event-status status-${event.status.toLowerCase()}`}>
                  {event.status}
                </span>
              </div>
              <div className="event-teams">
                {event.participants.map((p, i) => (
                  <span key={p.teamId}>
                    {i > 0 && ' vs. '}
                    {p.teamName}
                  </span>
                ))}
              </div>
              <div className="event-details">
                <span>{event.eventDate} {event.eventTime}</span>
                <span>{event.venueName}, {event.venueCity}</span>
              </div>
              <div className="event-meta">
                <span>{event.competitionName}</span>
                <span>{event.season} &middot; {event.stage}</span>
              </div>
              {event.results.length > 0 && (
                <div className="event-results">
                  {event.results.map((r) => (
                    <span key={r.teamId}>
                      {r.teamName}: {r.score}
                    </span>
                  ))}
                </div>
              )}
              <div className="event-actions">
                <button className="btn btn-danger btn-sm" onClick={() => handleDelete(event.id)}>
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default EventList;
