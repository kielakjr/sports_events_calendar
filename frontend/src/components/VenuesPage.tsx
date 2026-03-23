import { useEffect, useState } from 'react';
import type { Venue } from '../types/event';
import { fetchVenues, createVenue, deleteVenue } from '../api/venues';

const VenuesPage = () => {
  const [venues, setVenues] = useState<Venue[]>([]);
  const [name, setName] = useState('');
  const [city, setCity] = useState('');
  const [error, setError] = useState<string | null>(null);

  const load = () => {
    fetchVenues().then(setVenues).catch((err) => setError(err.message));
  };

  useEffect(load, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      await createVenue({ name, city });
      setName(''); setCity('');
      load();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to create');
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteVenue(id);
      setVenues((prev) => prev.filter((v) => v.id !== id));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to delete');
    }
  };

  return (
    <div>
      <div className="section-header">
        <h1>Venues</h1>
      </div>
      {error && <p className="error">{error}</p>}
      <form className="inline-form" onSubmit={handleCreate}>
        <input type="text" placeholder="Venue name" required value={name}
          onChange={(e) => setName(e.target.value)} />
        <input type="text" placeholder="City" required value={city}
          onChange={(e) => setCity(e.target.value)} />
        <button type="submit" className="btn btn-primary">Add</button>
      </form>
      <div className="item-list">
        {venues.map((v) => (
          <div key={v.id} className="item-row">
            <span>{v.name} <span className="item-detail">{v.city}</span></span>
            <button className="btn btn-danger btn-sm" onClick={() => handleDelete(v.id)}>Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default VenuesPage;
