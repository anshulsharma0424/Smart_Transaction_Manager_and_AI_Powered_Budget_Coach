import React, { useEffect, useState } from 'react';
import useGroupStore from '../stores/useGroupStore';
import { Link } from 'react-router-dom';
import { Plus } from 'lucide-react';
import CreateEventForm from '../components/groups/CreateEventForm'; 

const GroupsPage = () => {
  const { events, fetchEvents, loading, error } = useGroupStore();
  const [isFormOpen, setIsFormOpen] = useState(false);

  useEffect(() => {
    fetchEvents();
  }, [fetchEvents]);

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">My Groups & Events</h1>
        <button
          onClick={() => setIsFormOpen(true)}
          className="bg-[#7A7A73] text-white px-4 py-2 rounded-lg shadow-md hover:bg-[#57564F] flex items-center"
        >
          <Plus size={20} className="mr-2" />
          Create Event
        </button>
      </div>

      {error && <div className="bg-red-100 text-red-700 p-4 rounded-md mb-6">{error}</div>}
      {loading && events.length === 0 && <p>Loading your events...</p>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {events.map(event => (
          <Link to={`/groups/${event.id}`} key={event.id} className="block bg-[#f9f9f9] p-6 rounded-lg shadow-sm hover:shadow-lg transition-shadow duration-300">
            <h2 className="text-xl font-bold text-gray-900 truncate">{event.name}</h2>
            <p className="text-gray-600 mt-2 h-12 overflow-hidden">{event.description || 'No description'}</p>
            <p className="text-sm text-gray-400 mt-4">
              {new Date(event.eventDate).toLocaleDateString('en-IN', { year: 'numeric', month: 'long', day: 'numeric' })}
            </p>
            <div className="mt-4 text-sm font-semibold text-[#57564F]">View Details &rarr;</div>
          </Link>
        ))}
      </div>
      
      <CreateEventForm isOpen={isFormOpen} onClose={() => setIsFormOpen(false)} />
    </div>
  );
};

export default GroupsPage;