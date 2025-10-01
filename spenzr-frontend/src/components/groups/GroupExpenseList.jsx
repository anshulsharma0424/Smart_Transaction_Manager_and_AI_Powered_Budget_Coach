import React from 'react';
import useGroupStore from '../../stores/useGroupStore';

const GroupExpenseList = () => {
  const groupExpenses = useGroupStore((state) => state.groupExpenses);
  
  const formatCurrency = (amount) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);

  return (
    <div className="bg-[#f9f9f9] p-6 rounded-lg shadow-sm">
      <h3 className="text-lg font-bold text-gray-800 mb-4">Expenses</h3>
      <div className="space-y-4">
        {groupExpenses.length === 0 ? (
          <p className="text-gray-500">No expenses added to this event yet.</p>
        ) : (
          groupExpenses.map(exp => (
            <div key={exp.id} className="border-b border-gray-200 pb-4 last:border-b-0">
              <div className="flex justify-between items-center">
                <p className="font-semibold text-gray-700">{exp.description}</p>
                <p className="font-bold text-lg text-gray-800">{formatCurrency(exp.amount)}</p>
              </div>
              <p className="text-sm text-gray-500 mt-1">
                Paid by <span className="font-medium">{exp.paidByUserName}</span>
              </p>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default GroupExpenseList;