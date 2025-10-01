import React from 'react';
import useTransactionStore from '../../stores/useTransactionStore';
import { ArrowUpCircle, ArrowDownCircle, Banknote } from 'lucide-react';

const SummaryCards = () => {
  // CORRECTED: Select each piece of state individually.
  // This prevents the component from re-rendering unnecessarily.
  const summary = useTransactionStore((state) => state.summary);
  const loading = useTransactionStore((state) => state.loading);

  const formatCurrency = (amount) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);

  if (loading && !summary.totalIncome) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        {/* Skeleton Loader */}
        <div className="bg-white p-6 rounded-lg shadow-sm animate-pulse"><div className="h-6 bg-gray-200 rounded w-3/4"></div></div>
        <div className="bg-white p-6 rounded-lg shadow-sm animate-pulse"><div className="h-6 bg-gray-200 rounded w-3/4"></div></div>
        <div className="bg-white p-6 rounded-lg shadow-sm animate-pulse"><div className="h-6 bg-gray-200 rounded w-3/4"></div></div>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      {/* Total Income */}
      <div className="bg-[#f9f9f9] p-6 rounded-lg shadow-sm flex items-center">
        <div className="bg-green-100 p-3 rounded-full mr-4">
          <ArrowUpCircle className="h-6 w-6 text-green-600" />
        </div>
        <div>
          <p className="text-sm text-gray-500">Total Income</p>
          <p className="text-2xl font-semibold text-gray-800">{formatCurrency(summary.totalIncome)}</p>
        </div>
      </div>
      {/* Total Expense */}
      <div className="bg-[#f9f9f9] p-6 rounded-lg shadow-sm flex items-center">
        <div className="bg-red-100 p-3 rounded-full mr-4">
          <ArrowDownCircle className="h-6 w-6 text-red-600" />
        </div>
        <div>
          <p className="text-sm text-gray-500">Total Expense</p>
          <p className="text-2xl font-semibold text-gray-800">{formatCurrency(summary.totalExpense)}</p>
        </div>
      </div>
      {/* Net Amount */}
      <div className="bg-[#f9f9f9] p-6 rounded-lg shadow-sm flex items-center">
        <div className="bg-blue-100 p-3 rounded-full mr-4">
          <Banknote className="h-6 w-6 text-blue-600" />
        </div>
        <div>
          <p className="text-sm text-gray-500">Net Balance</p>
          <p className={`text-2xl font-semibold ${summary.netAmount >= 0 ? 'text-gray-800' : 'text-red-600'}`}>
            {formatCurrency(summary.netAmount)}
          </p>
        </div>
      </div>
    </div>
  );
};

export default SummaryCards;