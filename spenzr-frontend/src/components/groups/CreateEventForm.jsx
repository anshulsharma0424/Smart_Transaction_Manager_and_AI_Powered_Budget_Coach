import React, { useState } from 'react';
import useGroupStore from '../../stores/useGroupStore';
import { X } from 'lucide-react';

const CreateEventForm = ({ isOpen, onClose }) => {
  const { createEvent } = useGroupStore();
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [eventDate, setEventDate] = useState(new Date().toISOString().split('T')[0]);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await createEvent({ name, description, eventDate });
      onClose();
      setName('');
      setDescription('');
    } catch (err) {
      setError('Failed to create event.');
    }
  };
  
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-8 rounded-lg shadow-xl w-full max-w-md relative">
        <h2 className="text-2xl font-bold mb-6">Create New Event</h2>
        <button onClick={onClose} className="absolute top-4 right-4 text-gray-500 hover:text-gray-800"><X /></button>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="name" className="block text-sm font-medium text-gray-700">Event Name</label>
            <input type="text" id="name" value={name} onChange={(e) => setName(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded-md" required />
          </div>
          <div className="mb-4">
            <label htmlFor="description" className="block text-sm font-medium text-gray-700">Description</label>
            <input type="text" id="description" value={description} onChange={(e) => setDescription(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded-md" />
          </div>
          <div className="mb-6">
            <label htmlFor="eventDate" className="block text-sm font-medium text-gray-700">Date</label>
            <input type="date" id="eventDate" value={eventDate} onChange={(e) => setEventDate(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded-md" required />
          </div>
          {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
          <button type="submit" className="w-full bg-indigo-600 text-white py-2 rounded-md hover:bg-indigo-700">Create Event</button>
        </form>
      </div>
    </div>
  );
};

export default CreateEventForm;