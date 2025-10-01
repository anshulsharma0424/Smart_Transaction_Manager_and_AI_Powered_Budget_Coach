import React from 'react';
import useTransactionStore from '../../stores/useTransactionStore';
import { PlusCircle, MinusCircle } from 'lucide-react';

const TransactionList = () => {
  const transactions = useTransactionStore((state) => state.transactions);

  const formatCurrency = (amount) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);

  return (
    <div className="bg-[#f9f9f9] p-6 rounded-lg shadow-sm">
      <h2 className="text-xl font-bold text-gray-800 mb-4">Recent Transactions</h2>
      <div className="overflow-x-auto">
        <table className="w-full text-sm text-left text-gray-500">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th scope="col" className="px-6 py-3">Description</th>
              <th scope="col" className="px-6 py-3">Date</th>
              <th scope="col" className="px-6 py-3 text-right">Amount</th>
            </tr>
          </thead>
          <tbody>
            {transactions.length === 0 ? (
              <tr>
                <td colSpan="3" className="text-center py-8 text-gray-400">No transactions yet. Add one to get started!</td>
              </tr>
            ) : (
              transactions.map((tx) => (
                <tr key={tx.id} className="bg-[#f9f9f9] border-b">
                  <td className="px-6 py-4 font-medium text-gray-900 flex items-center">
                    {tx.type === 'INCOME' 
                      ? <PlusCircle className="h-5 w-5 text-green-500 mr-3" /> 
                      : <MinusCircle className="h-5 w-5 text-red-500 mr-3" />}
                    {tx.description}
                  </td>
                  <td className="px-6 py-4">
                    {new Date(tx.transactionDate).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}
                  </td>
                  <td className={`px-6 py-4 text-right font-semibold ${tx.type === 'INCOME' ? 'text-green-600' : 'text-red-600'}`}>
                    {tx.type === 'INCOME' ? '+' : '-'} {formatCurrency(tx.amount)}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TransactionList;