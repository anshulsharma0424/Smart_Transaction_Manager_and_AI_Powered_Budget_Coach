import { create } from 'zustand';
import apiClient from '../api/axios';

const useGroupStore = create((set, get) => ({
  events: [],
  currentEvent: null,
  groupExpenses: [],
  settlements: [],
  loading: false,
  error: null,

  // Action to fetch all events for the current user
  fetchEvents: async () => {
    set({ loading: true, error: null });
    try {
      const response = await apiClient.get('/events');
      set({ events: response.data, loading: false });
    } catch (error) {
      console.error("Failed to fetch events:", error);
      set({ error: 'Failed to load your events.', loading: false });
    }
  },

  // Action to create a new event
  createEvent: async (eventData) => {
    try {
      await apiClient.post('/events', eventData);
      get().fetchEvents(); // Refresh the list of events after creating a new one
    } catch (error) {
      console.error("Failed to create event:", error);
      throw error; // Re-throw the error so the form component can display a message
    }
  },

  // Action to fetch all data for a single, specific event
  fetchEventDetails: async (eventId) => {
    set({ loading: true, error: null, currentEvent: null, groupExpenses: [], settlements: [] });
    try {
      // Fetch all event-specific data in parallel for performance
      const [eventRes, expensesRes, settlementRes] = await Promise.all([
        apiClient.get(`/events/${eventId}`),
        apiClient.get(`/events/${eventId}/expenses`),
        apiClient.get(`/events/${eventId}/settlement`),
      ]);
      set({
        currentEvent: eventRes.data,
        groupExpenses: expensesRes.data,
        settlements: settlementRes.data,
        loading: false,
      });
    } catch (error) {
      console.error("Failed to fetch event details:", error);
      set({ error: 'Failed to load details for this event.', loading: false });
    }
  },
  
  // Action to add a new participant to the currently viewed event
  addParticipant: async (eventId, email) => {
    try {
      const response = await apiClient.post(`/events/${eventId}/participants?email=${email}`);
      set({ currentEvent: response.data }); // Update the current event state with the new participant list
    } catch (error) {
      console.error("Failed to add participant:", error);
      throw error;
    }
  },

  // Action to add a new expense to the currently viewed event
  addExpense: async (eventId, expenseData) => {
    try {
      await apiClient.post(`/events/${eventId}/expenses`, expenseData);
      // After adding an expense, refresh all data for the event detail page
      get().fetchEventDetails(eventId);
    } catch (error) {
      console.error("Failed to add expense:", error);
      throw error;
    }
  },
}));

export default useGroupStore;