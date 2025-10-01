import React, { useState, useEffect } from 'react';
import useTransactionStore from '../../stores/useTransactionStore';
import { X, Check } from 'lucide-react';

// 1. The form now accepts a `transactionToEdit` prop
const TransactionForm = ({ isOpen, onClose, transactionToEdit }) => {
  // Destructure the new `updateTransaction` function from the store
  const { categories, addTransaction, updateTransaction, addCategory, loading } = useTransactionStore();
  
  // Form state remains the same
  const [type, setType] = useState('EXPENSE');
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [transactionDate, setTransactionDate] = useState(new Date().toISOString().split('T')[0]);
  const [error, setError] = useState('');

  const [isCreatingCategory, setIsCreatingCategory] = useState(false);
  const [newCategoryName, setNewCategoryName] = useState('');
  
  // 2. This effect pre-fills the form when in "edit" mode
  useEffect(() => {
    if (isOpen) { // Only run logic when the modal is opened
      if (transactionToEdit) {
        // We are editing: fill the form with existing data
        setType(transactionToEdit.type);
        setDescription(transactionToEdit.description);
        setAmount(transactionToEdit.amount);
        setCategoryId(transactionToEdit.categoryId);
        setTransactionDate(transactionToEdit.transactionDate);
      } else {
        // We are creating: reset the form to its default state
        setType('EXPENSE');
        setDescription('');
        setAmount('');
        setCategoryId('');
        setTransactionDate(new Date().toISOString().split('T')[0]);
      }
      // Reset any errors when the modal opens
      setError('');
      setIsCreatingCategory(false);
    }
  }, [transactionToEdit, isOpen]); // Rerun this logic when the transaction to edit changes or modal opens

  // 3. The handleSubmit function is now smarter
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!description || !amount || !categoryId || !transactionDate) {
      setError('All fields are required.');
      return;
    }
    setError('');

    const transactionData = { type, description, amount: parseFloat(amount), categoryId, transactionDate };

    try {
      if (transactionToEdit) {
        // If we have a transactionToEdit, call the update action
        await updateTransaction(transactionToEdit.id, transactionData);
      } else {
        // Otherwise, call the add action as before
        await addTransaction(transactionData);
      }
      onClose();
    } catch (err) {
      setError(transactionToEdit ? 'Failed to update transaction.' : 'Failed to add transaction.');
    }
  };

  const handleSaveCategory = async () => {
    if (!newCategoryName.trim()) return;
    try {
      const newCategory = await addCategory({ name: newCategoryName, type });
      setCategoryId(newCategory.id);
      setNewCategoryName('');
      setIsCreatingCategory(false);
    } catch (err) {
      setError('Failed to save category.');
    }
  };

  if (!isOpen) return null;

  const currentCategories = type === 'EXPENSE' ? categories.expense : categories.income;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-8 rounded-lg shadow-xl w-full max-w-md relative">
        <button onClick={onClose} className="absolute top-4 right-4 text-gray-500 hover:text-gray-800"><X size={24} /></button>
        {/* 4. The title is now dynamic */}
        <h2 className="text-2xl font-bold mb-6 text-gray-800">
          {transactionToEdit ? 'Edit Transaction' : 'Add New Transaction'}
        </h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* The rest of the form's JSX does not need to change */}
          {/* ... (Transaction Type, Amount, Category, etc.) ... */}
          
           <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Type</label>
            <div className="flex gap-4">
              <button type="button" onClick={() => setType('EXPENSE')} className={`flex-1 py-2 px-4 rounded-md ${type === 'EXPENSE' ? 'bg-[#FF714B] text-white' : 'bg-gray-200'}`}>Expense</button>
              <button type="button" onClick={() => setType('INCOME')} className={`flex-1 py-2 px-4 rounded-md ${type === 'INCOME' ? 'bg-[#8ABB6C] text-white' : 'bg-gray-200'}`}>Income</button>
            </div>
          </div>

          <div>
            <label htmlFor="amount" className="block text-sm font-medium text-gray-700">Amount (₹)</label>
            <input type="number" step="0.01" id="amount" value={amount} onChange={(e) => setAmount(e.target.value)} className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm" required />
          </div>
          
          <div>
            <label htmlFor="category" className="block text-sm font-medium text-gray-700">Category</label>
            {!isCreatingCategory ? (
              <div className="flex items-center gap-2 mt-1">
                <select id="category" value={categoryId} onChange={(e) => setCategoryId(e.target.value)} className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm" required>
                  <option value="" disabled>Select a category</option>
                  {currentCategories?.map((cat) => (
                    <option key={cat.id} value={cat.id}>{cat.name}</option>
                  ))}
                </select>
                <button type="button" onClick={() => setIsCreatingCategory(true)} className="flex-shrink-0 text-sm text-[#7A7A73] hover:text-[#57564F] font-semibold whitespace-nowrap">
                  + New
                </button>
              </div>
            ) : (
              <div className="flex items-center gap-2 mt-1 p-2 border rounded-md bg-gray-50">
                <input type="text" placeholder="New category name" value={newCategoryName} onChange={(e) => setNewCategoryName(e.target.value)} className="block w-full px-3 py-1 border-gray-300 rounded-md shadow-sm" autoFocus />
                <button type="button" onClick={handleSaveCategory} className="p-1 text-green-600 hover:bg-green-100 rounded-full"><Check size={20} /></button>
                <button type="button" onClick={() => setIsCreatingCategory(false)} className="p-1 text-red-600 hover:bg-red-100 rounded-full"><X size={20} /></button>
              </div>
            )}
          </div>

          <div>
            <label htmlFor="description" className="block text-sm font-medium text-gray-700">Description</label>
            <input type="text" id="description" value={description} onChange={(e) => setDescription(e.target.value)} className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm" required />
          </div>
          
          <div>
            <label htmlFor="date" className="block text-sm font-medium text-gray-700">Date</label>
            <input type="date" id="date" value={transactionDate} onChange={(e) => setTransactionDate(e.target.value)} className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm" required />
          </div>

          {error && <p className="text-red-500 text-sm">{error}</p>}
          
          <button type="submit" disabled={loading} className="w-full bg-[#7A7A73] text-white py-2 px-4 rounded-md hover:bg-[#57564F] ">
            {loading ? 'Saving...' : 'Save Transaction'}
          </button>

        </form>
      </div>
    </div>
  );
};

export default TransactionForm;