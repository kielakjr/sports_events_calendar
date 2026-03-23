export type Page = 'events' | 'sports' | 'teams' | 'competitions' | 'venues';

interface Props {
  current: Page;
  onChange: (page: Page) => void;
}

const PAGES: { key: Page; label: string }[] = [
  { key: 'events', label: 'Events' },
  { key: 'sports', label: 'Sports' },
  { key: 'teams', label: 'Teams' },
  { key: 'competitions', label: 'Competitions' },
  { key: 'venues', label: 'Venues' },
];

const Navbar = ({ current, onChange }: Props) => {
  return (
    <nav className="navbar">
      <div className="navbar-brand">Sports Events</div>
      <ul className="navbar-links">
        {PAGES.map((p) => (
          <li key={p.key}>
            <a
              href="#"
              className={current === p.key ? 'active' : ''}
              onClick={(e) => { e.preventDefault(); onChange(p.key); }}
            >
              {p.label}
            </a>
          </li>
        ))}
      </ul>
    </nav>
  );
};

export default Navbar;
