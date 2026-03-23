import { useEffect, useState } from 'react';
import type { Team, Sport } from '../types/event';
import { fetchTeams, createTeam, deleteTeam } from '../api/teams';
import { fetchSports } from '../api/sports';

const TeamsPage = () => {
  const [teams, setTeams] = useState<Team[]>([]);
  const [sports, setSports] = useState<Sport[]>([]);
  const [error, setError] = useState<string | null>(null);

  const [name, setName] = useState('');
  const [officialName, setOfficialName] = useState('');
  const [abbreviation, setAbbreviation] = useState('');
  const [countryCode, setCountryCode] = useState('');
  const [sportId, setSportId] = useState(0);

  const load = () => {
    Promise.all([fetchTeams(), fetchSports()])
      .then(([t, s]) => { setTeams(t); setSports(s); })
      .catch((err) => setError(err.message));
  };

  useEffect(load, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      await createTeam({
        name,
        officialName,
        abbreviation,
        teamCountryCode: countryCode,
        sport: { id: sportId },
      });
      setName(''); setOfficialName(''); setAbbreviation(''); setCountryCode(''); setSportId(0);
      load();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to create');
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteTeam(id);
      setTeams((prev) => prev.filter((t) => t.id !== id));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Failed to delete');
    }
  };

  return (
    <div>
      <div className="section-header">
        <h1>Teams</h1>
      </div>
      {error && <p className="error">{error}</p>}
      <form className="form" onSubmit={handleCreate}>
        <div className="form-row">
          <label>
            Name
            <input type="text" required value={name} onChange={(e) => setName(e.target.value)} />
          </label>
          <label>
            Official Name
            <input type="text" required value={officialName} onChange={(e) => setOfficialName(e.target.value)} />
          </label>
        </div>
        <div className="form-row">
          <label>
            Abbreviation
            <input type="text" required maxLength={5} value={abbreviation}
              onChange={(e) => setAbbreviation(e.target.value)} />
          </label>
          <label>
            Country Code
            <input type="text" required maxLength={3} value={countryCode}
              onChange={(e) => setCountryCode(e.target.value)} />
          </label>
          <label>
            Sport
            <select required value={sportId} onChange={(e) => setSportId(Number(e.target.value))}>
              <option value={0} disabled>Select...</option>
              {sports.map((s) => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
          </label>
        </div>
        <div className="form-actions">
          <button type="submit" className="btn btn-primary">Add Team</button>
        </div>
      </form>
      <div className="item-list">
        {teams.map((t) => (
          <div key={t.id} className="item-row">
            <div>
              <strong>{t.name}</strong>
              <span className="item-detail">{t.abbreviation} &middot; {t.teamCountryCode} &middot; {t.sport?.name}</span>
            </div>
            <button className="btn btn-danger btn-sm" onClick={() => handleDelete(t.id)}>Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TeamsPage;
