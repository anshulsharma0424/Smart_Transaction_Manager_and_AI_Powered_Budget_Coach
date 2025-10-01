import React, { useEffect, useState } from 'react';
import useTransactionStore from '../stores/useTransactionStore';
import TransactionForm from '../components/dashboard/TransactionForm';
import { Plus, Edit, Trash2 } from 'lucide-react';

const TransactionsPage = () => {
  const { transactions, fetchTransactions, deleteTransaction, loading, error } = useTransactionStore();
  
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [transactionToEdit, setTransactionToEdit] = useState(null);

  useEffect(() => {
    fetchTransactions();
  }, [fetchTransactions]);

  const handleAddNew = () => {
    setTransactionToEdit(null);
    setIsFormOpen(true);
  };
  
  const handleEdit = (transaction) => {
    setTransactionToEdit(transaction);
    setIsFormOpen(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this transaction?')) {
      await deleteTransaction(id);
    }
  };
  
  const formatCurrency = (amount) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Manage Transactions</h1>
        <button onClick={handleAddNew} className="bg-[#7A7A73] text-white px-4 py-2 rounded-lg shadow-md hover:bg-[#57564F] flex items-center">
          <Plus size={20} className="mr-2" />
          Add Transaction
        </button>
      </div>
      
      {error && <div className="bg-red-100 text-red-700 p-4 rounded-md mb-6">{error}</div>}
      
      <div className="bg-white rounded-lg shadow-sm overflow-hidden">
        <table className="w-full text-sm text-left text-gray-500">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th scope="col" className="px-6 py-3">Description</th>
              <th scope="col" className="px-6 py-3">Date</th>
              <th scope="col" className="px-6 py-3">Type</th>
              <th scope="col" className="px-6 py-3 text-right">Amount</th>
              <th scope="col" className="px-6 py-3 text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading && transactions.length === 0 ? (
              <tr><td colSpan="5" className="text-center py-8">Loading...</td></tr>
            ) : (
              transactions.map((tx) => (
                <tr key={tx.id} className="bg-[#f9f9f9] border-b hover:bg-gray-50">
                  <td className="px-6 py-4 font-medium text-gray-900">{tx.description}</td>
                  <td className="px-6 py-4">{new Date(tx.transactionDate).toLocaleDateString('en-IN')}</td>
                  <td className="px-6 py-4">
                    <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                      tx.type === 'INCOME' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    }`}>
                      {tx.type}
                    </span>
                  </td>
                  <td className={`px-6 py-4 text-right font-medium ${tx.type === 'INCOME' ? 'text-green-600' : 'text-red-600'}`}>
                    {formatCurrency(tx.amount)}
                  </td>
                  <td className="px-6 py-4 text-center">
                    <button onClick={() => handleEdit(tx)} className="text-[#57564F] hover:text-indigo-900 mr-4"><Edit size={16}/></button>
                    <button onClick={() => handleDelete(tx.id)} className="text-red-600 hover:text-red-900"><Trash2 size={16}/></button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <TransactionForm 
        isOpen={isFormOpen} 
        onClose={() => setIsFormOpen(false)} 
        transactionToEdit={transactionToEdit} 
      />
    </div>
  );
};

export default TransactionsPage;