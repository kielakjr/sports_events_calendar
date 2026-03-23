import { useEffect, useState } from 'react';
import type { Competition } from '../types/event';
import { fetchCompetitions, createCompetition, deleteCompetition } from '../api/competitions';

const CompetitionsPage = () => {
  const [competitions, setCompetitions] = useState<Competition[]>([]);
  const [name, setName] = useState('');
  const [error, setError] = useState<string | null>(null);

  const load = () => {
    fetchCompetitions().then(setCompetitions).catch((err) => setError(err.message));
  };

  useEffect(load, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      await createCompetition({ name });
      setName('');
      load();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to create');
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteCompetition(id);
      setCompetitions((prev) => prev.filter((c) => c.id !== id));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to delete');
    }
  };

  return (
    <div>
      <div className="section-header">
        <h1>Competitions</h1>
      </div>
      {error && <p className="error">{error}</p>}
      <form className="inline-form" onSubmit={handleCreate}>
        <input type="text" placeholder="Competition name" required value={name}
          onChange={(e) => setName(e.target.value)} />
        <button type="submit" className="btn btn-primary">Add</button>
      </form>
      <div className="item-list">
        {competitions.map((c) => (
          <div key={c.id} className="item-row">
            <span>{c.name}</span>
            <button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.id)}>Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CompetitionsPage;
