import React, { useState } from 'react';
import useGroupStore from '../../stores/useGroupStore';
import { X } from 'lucide-react';
import keycloak from '../../keycloak';

const AddGroupExpenseForm = ({ eventId, isOpen, onClose }) => {
  const { addExpense, currentEvent } = useGroupStore();
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState('');
  const [paidByUserId, setPaidByUserId] = useState(keycloak.subject); // Default to current user
  const [expenseDate, setExpenseDate] = useState(new Date().toISOString().split('T')[0]);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!paidByUserId) {
      setError('Please select who paid.');
      return;
    }
    try {
      await addExpense(eventId, { description, amount: parseFloat(amount), expenseDate, paidByUserId });
      onClose();
      setDescription('');
      setAmount('');
    } catch (err) {
      setError('Failed to add expense.');
    }
  };
  
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-8 rounded-lg shadow-xl w-full max-w-md relative">
        <h2 className="text-2xl font-bold mb-6">Add Group Expense</h2>
        <button onClick={onClose} className="absolute top-4 right-4 text-gray-500 hover:text-gray-800"><X /></button>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="g_desc" className="block text-sm font-medium">Description</label>
            <input type="text" id="g_desc" value={description} onChange={(e) => setDescription(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded-md" required />
          </div>
          <div className="mb-4">
            <label htmlFor="g_amount" className="block text-sm font-medium">Amount (₹)</label>
            <input type="number" id="g_amount" value={amount} onChange={(e) => setAmount(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded-md" required />
          </div>
          <div className="mb-4">
            <label htmlFor="g_paidBy" className="block text-sm font-medium">Paid By</label>
            <select id="g_paidBy" value={paidByUserId} onChange={(e) => setPaidByUserId(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded-md" required>
              {currentEvent?.participants.map(p => (
                <option key={p.userId} value={p.userId}>{p.name}</option>
              ))}
            </select>
          </div>
           <div className="mb-6">
            <label htmlFor="g_date" className="block text-sm font-medium">Date</label>
            <input type="date" id="g_date" value={expenseDate} onChange={(e) => setExpenseDate(e.target.value)} className="mt-1 block w-full p-2 border border-gray-300 rounded-md" required />
          </div>
          {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
          <button type="submit" className="w-full bg-[#7A7A73] text-white py-2 rounded-md hover:bg-[#57564F]">Add Expense</button>
        </form>
      </div>
    </div>
  );
};

export default AddGroupExpenseForm;