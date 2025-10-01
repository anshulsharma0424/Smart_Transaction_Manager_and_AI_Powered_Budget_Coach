import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import useGroupStore from '../stores/useGroupStore';
import { Plus, ArrowLeft } from 'lucide-react';
import AddParticipantForm from '../components/groups/AddParticipantForm';
import AddGroupExpenseForm from '../components/groups/AddGroupExpenseForm';
import ParticipantList from '../components/groups/ParticipantList';
import GroupExpenseList from '../components/groups/GroupExpenseList';
import SettlementSummary from '../components/groups/SettlementSummary';

const EventDetailPage = () => {
  const { eventId } = useParams();
  const { currentEvent, fetchEventDetails, loading, error } = useGroupStore();
  const [isParticipantFormOpen, setIsParticipantFormOpen] = useState(false);
  const [isExpenseFormOpen, setIsExpenseFormOpen] = useState(false);

  useEffect(() => {
    if (eventId) {
      fetchEventDetails(eventId);
    }
  }, [eventId, fetchEventDetails]);

  if (loading && !currentEvent) {
    return <p>Loading event details...</p>;
  }

  if (error) {
    return <div className="bg-red-100 text-red-700 p-4 rounded-md">{error}</div>;
  }
  
  if (!currentEvent) {
    return <p>Event not found. <Link to="/groups" className="text-indigo-600">Go back</Link></p>;
  }

  return (
    <div>
      <Link to="/groups" className="flex items-center text-sm text-gray-500 hover:text-gray-800 mb-4">
        <ArrowLeft size={16} className="mr-1" />
        Back to all events
      </Link>
      <div className="mb-8">
        <h1 className="text-4xl font-bold text-gray-800">{currentEvent.name}</h1>
        <p className="text-lg text-gray-500 mt-2">{currentEvent.description}</p>
      </div>

      <div className="flex flex-wrap gap-4 mb-8">
        <button onClick={() => setIsParticipantFormOpen(true)} className="bg-[#7A7A73] text-white px-4 py-2 rounded-lg shadow-md hover:bg-[#57564F] flex items-center">
          <Plus size={20} className="mr-2" /> Add Participant
        </button>
        <button onClick={() => setIsExpenseFormOpen(true)} className="bg-red-400 text-white px-4 py-2 rounded-lg shadow-md hover:bg-red-500 flex items-center">
          <Plus size={20} className="mr-2" /> Add Expense
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-8">
          <SettlementSummary />
          <GroupExpenseList />
        </div>
        <div className="lg:col-span-1">
          <ParticipantList />
        </div>
      </div>

      <AddParticipantForm eventId={eventId} isOpen={isParticipantFormOpen} onClose={() => setIsParticipantFormOpen(false)} />
      <AddGroupExpenseForm eventId={eventId} isOpen={isExpenseFormOpen} onClose={() => setIsExpenseFormOpen(false)} />
    </div>
  );
};

export default EventDetailPage;