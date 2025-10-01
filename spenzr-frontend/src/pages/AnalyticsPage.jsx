import React, { useEffect, useState } from 'react';
import useTransactionStore from '../stores/useTransactionStore';
import CategoryDonutChart from '../components/analytics/CategoryDonutChart';
import KeyInsights from '../components/analytics/KeyInsights';
import TopTransactions from '../components/analytics/TopTransactions';
// We'll uncomment this in a future step if we build it
// import TrendBarChart from '../components/analytics/TrendBarChart';

const AnalyticsPage = () => {
  // CORRECTED: Import the new, specific fetch functions
  const { 
    fetchTransactions, 
    fetchCategories, 
    calculateAnalytics, 
    analyticsData, 
    loading 
  } = useTransactionStore();
  
  const [filter, setFilter] = useState('This Month');

  useEffect(() => {
    const fetchDataAndCalculate = async () => {
      // CORRECTED: Call the specific functions to get the data needed for this page
      await Promise.all([fetchTransactions(), fetchCategories()]);
      // The calculateAnalytics function will now work because the store has the necessary data
      calculateAnalytics(filter);
    };
    fetchDataAndCalculate();
  }, [fetchTransactions, fetchCategories, calculateAnalytics, filter]);

  const handleFilterChange = (newFilter) => {
    setFilter(newFilter);
  };
  
  const filters = ['This Month', 'Last 6 Months', 'This Year'];

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-800 mb-6">Financial Analytics</h1>

      <div className="mb-6 flex space-x-2">
        {filters.map(f => (
          <button
            key={f}
            onClick={() => handleFilterChange(f)}
            className={`px-4 py-2 text-sm font-medium rounded-md ${filter === f ? 'bg-[#57564F] text-white' : 'bg-white text-gray-700 hover:bg-[#8f8f8f] hover:text-white'}`}
          >
            {f}
          </button>
        ))}
      </div>

      {loading ? (
        <p>Loading analytics...</p>
      ) : (
        <div className="space-y-8">
          <KeyInsights insights={analyticsData.insights} />
          <div className="grid grid-cols-1 lg:grid-cols-5 gap-8">
            <div className="lg:col-span-2 bg-[#f9f9f9] p-6 rounded-lg shadow-sm">
              <h2 className="text-lg font-bold text-gray-800 mb-4">Expenses by Category</h2>
              <CategoryDonutChart data={analyticsData.byCategory} />
            </div>
            <div className="lg:col-span-3">
              <TopTransactions transactions={analyticsData.topTransactions} />
            </div>
          </div>
          {/* // Placeholder for the trend chart
          <div>
            <h2 className="text-lg font-bold text-gray-800 mb-4">Income vs. Expense Trend</h2>
            <TrendBarChart data={analyticsData.trend} />
          </div> 
          */}
        </div>
      )}
    </div>
  );
};

export default AnalyticsPage;