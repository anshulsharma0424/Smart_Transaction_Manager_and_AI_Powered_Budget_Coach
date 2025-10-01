import React from 'react';
import useGroupStore from '../../stores/useGroupStore';
import { User } from 'lucide-react';

const ParticipantList = () => {
  const participants = useGroupStore((state) => state.currentEvent?.participants || []);

  return (
    <div className="bg-[#f9f9f9] p-6 rounded-lg shadow-sm">
      <h3 className="text-lg font-bold text-gray-800 mb-4">Participants ({participants.length})</h3>
      <ul className="space-y-3">
        {participants.map(p => (
          <li key={p.userId} className="flex items-center">
            <div className="bg-gray-200 p-2 rounded-full mr-3">
              <User className="h-5 w-5 text-gray-600" />
            </div>
            <span className="text-gray-700">{p.name}</span>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ParticipantList;