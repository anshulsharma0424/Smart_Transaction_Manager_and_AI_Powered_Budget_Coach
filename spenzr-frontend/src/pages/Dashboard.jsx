import React, { useEffect, useState } from 'react';
import useTransactionStore from '../stores/useTransactionStore';
import SummaryCards from '../components/dashboard/SummaryCards';
import TransactionList from '../components/dashboard/TransactionList';
import TransactionForm from '../components/dashboard/TransactionForm';
import { Plus } from 'lucide-react';

const Dashboard = () => {
  // We no longer need to fetch suggestions on this page
  const fetchDashboardData = useTransactionStore((state) => state.fetchDashboardData);
  const error = useTransactionStore((state) => state.error);
  
  const [isFormOpen, setIsFormOpen] = useState(false);

  useEffect(() => {
    let ignore = false;
    if (!ignore) {
      fetchDashboardData();
    }
    return () => {
      ignore = true;
    };
  }, [fetchDashboardData]);

  return (
    <div className="space-y-8">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Dashboard</h1>
        <button
          onClick={() => setIsFormOpen(true)}
          className="bg-[#7A7A73] text-white px-4 py-2 rounded-lg shadow-md hover:bg-[#57564F] flex items-center"
        >
          <Plus size={20} className="mr-2" />
          Add Transaction
        </button>
      </div>

      {error && <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">{error}</div>}
      
      <SummaryCards />

      {/* The Transaction List now takes up the full width below the summary cards */}
      <TransactionList />

      <TransactionForm isOpen={isFormOpen} onClose={() => setIsFormOpen(false)} />
    </div>
  );
};

export default Dashboard;