import React from 'react';

const TopTransactions = ({ transactions }) => {
  const formatCurrency = (amount) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);

  return (
    <div className="bg-[#f9f9f9] p-6 rounded-lg shadow-sm">
      <h3 className="text-lg font-bold text-gray-800 mb-4">Top 5 Expenses</h3>
      <ul className="space-y-3">
        {transactions.map(tx => (
          <li key={tx.id} className="flex justify-between items-center border-b pb-2 last:border-0">
            <span className="text-gray-700">{tx.description}</span>
            <span className="font-semibold text-red-600">{formatCurrency(tx.amount)}</span>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TopTransactions;