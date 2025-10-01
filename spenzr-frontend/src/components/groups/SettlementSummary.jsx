import React from 'react';
import useGroupStore from '../../stores/useGroupStore';
import { ArrowRight } from 'lucide-react';

const SettlementSummary = () => {
  const settlements = useGroupStore((state) => state.settlements);
  
  const formatCurrency = (amount) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);

  return (
    <div className="bg-[#f9f9f9] p-6 rounded-lg shadow-sm">
      <h3 className="text-lg font-bold text-gray-800 mb-4">Settlements</h3>
      <div className="space-y-3">
        {settlements.length === 0 ? (
          <p className="text-green-600 font-medium">All settled up!</p>
        ) : (
          settlements.map((s, index) => (
            <div key={index} className="flex items-center justify-between bg-gray-50 p-4 rounded-md">
              <div className="flex items-center">
                <span className="font-medium text-red-600">{s.from.name}</span>
                <ArrowRight className="h-4 w-4 mx-3 text-gray-400" />
                <span className="font-medium text-green-600">{s.to.name}</span>
              </div>
              <span className="font-bold text-gray-800 text-lg">{formatCurrency(s.amount)}</span>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default SettlementSummary;