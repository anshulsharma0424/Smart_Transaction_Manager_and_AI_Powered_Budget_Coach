import React from 'react';
import { TrendingUp, Wallet, IndianRupee } from 'lucide-react';

const KeyInsights = ({ insights }) => {
  const formatCurrency = (amount) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div className="bg-[#f9f9f9] p-4 rounded-lg shadow-sm">
        <h3 className="font-semibold text-gray-600">Biggest Spending Category</h3>
        <div className="flex items-center mt-2">
          <TrendingUp className="h-6 w-6 text-indigo-500 mr-3" />
          <p className="text-2xl font-bold text-gray-800">{insights.biggestCategory || 'N/A'}</p>
        </div>
      </div>
      <div className="bg-[#f9f9f9] p-4 rounded-lg shadow-sm">
        <h3 className="font-semibold text-gray-600">Average Daily Spending</h3>
        <div className="flex items-center mt-2">
          <IndianRupee className="h-6 w-6 text-indigo-500 mr-3" />
          <p className="text-2xl font-bold text-gray-800">{formatCurrency(insights.dailyAverage || 0)}</p>
        </div>
      </div>
    </div>
  );
};

export default KeyInsights;