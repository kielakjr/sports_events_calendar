import { useState } from 'react';
import Navbar from './components/Navbar';
import type { Page } from './components/Navbar';
import type { SportEvent } from './types/event';
import EventList from './components/EventList';
import EventForm from './components/EventForm';
import SportsPage from './components/SportsPage';
import TeamsPage from './components/TeamsPage';
import CompetitionsPage from './components/CompetitionsPage';
import VenuesPage from './components/VenuesPage';

const App = () => {
  const [page, setPage] = useState<Page>('events');
  const [showEventForm, setShowEventForm] = useState(false);
  const [editingEvent, setEditingEvent] = useState<SportEvent | undefined>();
  const [refreshKey, setRefreshKey] = useState(0);

  const closeForm = () => {
    setShowEventForm(false);
    setEditingEvent(undefined);
  };

  const renderPage = () => {
    switch (page) {
      case 'events':
        return showEventForm ? (
          <EventForm
            editEvent={editingEvent}
            onCreated={() => { closeForm(); setRefreshKey((k) => k + 1); }}
            onCancel={closeForm}
          />
        ) : (
          <EventList
            onAdd={() => setShowEventForm(true)}
            onEdit={(event) => { setEditingEvent(event); setShowEventForm(true); }}
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
      <Navbar current={page} onChange={(p) => { setPage(p); closeForm(); }} />
      <main className="content">
        {renderPage()}
      </main>
    </>
  );
};

export default App;
