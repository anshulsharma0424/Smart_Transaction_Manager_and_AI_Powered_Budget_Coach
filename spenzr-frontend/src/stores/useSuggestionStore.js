import { create } from 'zustand';
import apiClient from '../api/axios';

const useSuggestionStore = create((set) => ({
  suggestions: [],
  loading: false,
  error: null,

  // Action to fetch all suggestions for the logged-in user
  fetchSuggestions: async () => {
    set({ loading: true, error: null });
    try {
      const response = await apiClient.get('/suggestions');
      set({ suggestions: response.data, loading: false });
    } catch (error) {
      console.error("Failed to fetch suggestions:", error);
      set({ error: 'Failed to load AI suggestions.', loading: false });
    }
  },
}));

export default useSuggestionStore;