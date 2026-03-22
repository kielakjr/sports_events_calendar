import Navbar from './components/Navbar';
import EventList from './components/EventList';

const App = () => {
  return (
    <>
      <Navbar />
      <main className="content">
        <h1>Upcoming Events</h1>
        <EventList />
      </main>
    </>
  );
};

export default App;
