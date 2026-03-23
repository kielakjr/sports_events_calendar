import { useEffect, useState } from 'react';
import type { Sport } from '../types/event';
import { fetchSports, createSport, deleteSport } from '../api/sports';

const SportsPage = () => {
  const [sports, setSports] = useState<Sport[]>([]);
  const [name, setName] = useState('');
  const [error, setError] = useState<string | null>(null);

  const load = () => {
    fetchSports().then(setSports).catch((err) => setError(err.message));
  };

  useEffect(load, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      await createSport({ name });
      setName('');
      load();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to create');
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteSport(id);
      setSports((prev) => prev.filter((s) => s.id !== id));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to delete');
    }
  };

  return (
    <div>
      <div className="section-header">
        <h1>Sports</h1>
      </div>
      {error && <p className="error">{error}</p>}
      <form className="inline-form" onSubmit={handleCreate}>
        <input type="text" placeholder="Sport name" required value={name}
          onChange={(e) => setName(e.target.value)} />
        <button type="submit" className="btn btn-primary">Add</button>
      </form>
      <div className="item-list">
        {sports.map((s) => (
          <div key={s.id} className="item-row">
            <span>{s.name}</span>
            <button className="btn btn-danger btn-sm" onClick={() => handleDelete(s.id)}>Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SportsPage;
