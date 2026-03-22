import { useEffect, useState } from 'react';
import type { SportEvent } from '../types/event';
import { fetchEvents } from '../api/events';

const EventList = () => {
  const [events, setEvents] = useState<SportEvent[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchEvents()
      .then(setEvents)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading events...</p>;
  if (error) return <p className="error">Error: {error}</p>;
  if (events.length === 0) return <p>No events found.</p>;

  return (
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
        </div>
      ))}
    </div>
  );
};

export default EventList;
