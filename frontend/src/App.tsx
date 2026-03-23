import { useState } from 'react';
import Navbar from './components/Navbar';
import type { Page } from './components/Navbar';
import EventList from './components/EventList';
import EventForm from './components/EventForm';
import SportsPage from './components/SportsPage';
import TeamsPage from './components/TeamsPage';
import CompetitionsPage from './components/CompetitionsPage';
import VenuesPage from './components/VenuesPage';

const App = () => {
  const [page, setPage] = useState<Page>('events');
  const [showEventForm, setShowEventForm] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);

  const renderPage = () => {
    switch (page) {
      case 'events':
        return showEventForm ? (
          <EventForm
            onCreated={() => { setShowEventForm(false); setRefreshKey((k) => k + 1); }}
            onCancel={() => setShowEventForm(false)}
          />
        ) : (
          <EventList
            onAdd={() => setShowEventForm(true)}
            refreshKey={refreshKey}
          />
        );
      case 'sports':
        return <SportsPage />;
      case 'teams':
        return <TeamsPage />;
      case 'competitions':
        return <CompetitionsPage />;
      case 'venues':
        return <VenuesPage />;
    }
  };

  return (
    <>
      <Navbar current={page} onChange={(p) => { setPage(p); setShowEventForm(false); }} />
      <main className="content">
        {renderPage()}
      </main>
    </>
  );
};

export default App;
