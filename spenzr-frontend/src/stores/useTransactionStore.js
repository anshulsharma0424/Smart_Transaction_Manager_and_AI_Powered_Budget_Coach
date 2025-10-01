import { create } from 'zustand';
import apiClient from '../api/axios';

const useTransactionStore = create((set, get) => ({
  // --- STATE ---
  transactions: [],
  categories: { income: [], expense: [] },
  summary: { totalIncome: 0, totalExpense: 0, netAmount: 0 },
  loading: false,
  error: null,
  analyticsData: {
    byCategory: [],
    trend: [],
    insights: {},
    topTransactions: [],
  },

  // --- CORE FETCH ACTIONS ---
  
  fetchSummary: async () => {
    const res = await apiClient.get('/reports/summary');
    set({ summary: res.data });
  },
  
  fetchTransactions: async () => {
    const res = await apiClient.get('/transactions');
    set({ transactions: res.data });
  },

  fetchCategories: async () => {
    const res = await apiClient.get('/categories');
    const incomeCategories = res.data.filter(c => c.type === 'INCOME');
    const expenseCategories = res.data.filter(c => c.type === 'EXPENSE');
    set({ categories: { income: incomeCategories, expense: expenseCategories } });
  },

  fetchDashboardData: async () => {
    set({ loading: true, error: null });
    try {
      await Promise.all([
        get().fetchSummary(),
        get().fetchTransactions(),
        get().fetchCategories(),
      ]);
    } catch (error) {
      console.error("Failed to fetch dashboard data:", error);
      set({ error: 'Failed to load dashboard data.' });
    } finally {
      set({ loading: false });
    }
  },

  // --- CRUD ACTIONS ---

  addTransaction: async (newTransaction) => {
    try {
      await apiClient.post('/transactions', newTransaction);
      get().fetchDashboardData();
    } catch (error) {
      console.error("Failed to add transaction:", error);
      throw error;
    }
  },

  updateTransaction: async (id, transactionData) => {
    try {
      await apiClient.put(`/transactions/${id}`, transactionData);
      get().fetchDashboardData(); 
    } catch (error) {
      console.error("Failed to update transaction:", error);
      throw error;
    }
  },

  deleteTransaction: async (id) => {
    try {
      await apiClient.delete(`/transactions/${id}`);
      get().fetchDashboardData();
    } catch (error) {
      console.error("Failed to delete transaction:", error);
      throw error;
    }
  },

  addCategory: async (categoryData) => {
    try {
      const response = await apiClient.post('/categories', categoryData);
      await get().fetchCategories();
      return response.data;
    } catch (error) {
      console.error("Failed to add category:", error);
      throw error;
    }
  },

  // --- ANALYTICS ACTION ---

  calculateAnalytics: (period) => {
    set({ loading: true });
    const allTransactions = get().transactions;
    const { expense: expenseCategories } = get().categories;
    const now = new Date();
    let startDate = new Date();

    if (period === 'This Month') {
      startDate = new Date(now.getFullYear(), now.getMonth(), 1);
    } else if (period === 'Last 6 Months') {
      startDate = new Date(now.getFullYear(), now.getMonth() - 6, 1);
    } else if (period === 'This Year') {
      startDate = new Date(now.getFullYear(), 0, 1);
    }

    const filtered = allTransactions.filter(t => new Date(t.transactionDate) >= startDate);
    
    const categoryMap = expenseCategories.reduce((acc, cat) => {
      acc[cat.id] = cat.name;
      return acc;
    }, {});

    const byCategory = filtered
      .filter(t => t.type === 'EXPENSE')
      .reduce((acc, t) => {
        const name = categoryMap[t.categoryId] || 'Uncategorized';
        acc[name] = (acc[name] || 0) + t.amount;
        return acc;
      }, {});

    const byCategoryChartData = Object.entries(byCategory).map(([name, value]) => ({ name, value }));

    const topTransactions = filtered
      .filter(t => t.type === 'EXPENSE')
      .sort((a, b) => b.amount - a.amount)
      .slice(0, 5);

    const biggestCategory = byCategoryChartData.length > 0 
      ? byCategoryChartData.reduce((max, cat) => cat.value > max.value ? cat : max)
      : { name: 'N/A' };
      
    const totalDays = Math.max(1, (now.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    const totalSpending = filtered.filter(t => t.type === 'EXPENSE').reduce((sum, t) => sum + t.amount, 0);
    const dailyAverage = totalSpending / totalDays;
    
    set({
      analyticsData: {
        byCategory: byCategoryChartData,
        trend: [],
        topTransactions,
        insights: {
          biggestCategory: biggestCategory.name,
          dailyAverage: dailyAverage,
        },
      },
      loading: false
    });
  },
}));

export default useTransactionStore;