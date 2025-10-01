import React, { useState } from 'react';
import useGroupStore from '../../stores/useGroupStore';
import { X } from 'lucide-react';

const AddParticipantForm = ({ eventId, isOpen, onClose }) => {
  const { addParticipant } = useGroupStore();
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await addParticipant(eventId, email);
      onClose();
      setEmail('');
    } catch (err) {
      setError('Failed to add participant. Make sure the user exists and the email is correct.');
    }
  };
  
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-8 rounded-lg shadow-xl w-full max-w-md relative">
        <h2 className="text-2xl font-bold mb-6">Add Participant</h2>
        <button onClick={onClose} className="absolute top-4 right-4 text-gray-500 hover:text-gray-800"><X /></button>
        <form onSubmit={handleSubmit}>
          <p className="text-sm text-gray-600 mb-4">Enter the email of the Spenzr user you want to add to this event.</p>
          <div className="mb-6">
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">User Email</label>
            <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} className="mt-1 block w-full p-2 border rounded-md" required />
          </div>
          {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
          <button type="submit" className="w-full bg-[#7A7A73] text-white py-2 rounded-md hover:bg-[#57564F]">Add User</button>
        </form>
      </div>
    </div>
  );
};

export default AddParticipantForm;