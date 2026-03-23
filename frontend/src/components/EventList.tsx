import { useEffect, useState } from 'react';
import type { SportEvent } from '../types/event';
import { fetchEvents, deleteEvent } from '../api/events';

interface Props {
  onAdd: () => void;
  refreshKey: number;
}

const EventList = ({ onAdd, refreshKey }: Props) => {
  const [events, setEvents] = useState<SportEvent[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = () => {
    setLoading(true);
    fetchEvents()
      .then(setEvents)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(load, [refreshKey]);

  const handleDelete = async (id: number) => {
    try {
      await deleteEvent(id);
      setEvents((prev) => prev.filter((e) => e.id !== id));
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Delete failed';
      setError(msg);
    }
  };

  if (loading) return <p>Loading events...</p>;
  if (error) return <p className="error">Error: {error}</p>;

  return (
    <div>
      <div className="section-header">
        <h1>Events</h1>
        <button className="btn btn-primary" onClick={onAdd}>+ New Event</button>
      </div>
      {events.length === 0 ? (
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
