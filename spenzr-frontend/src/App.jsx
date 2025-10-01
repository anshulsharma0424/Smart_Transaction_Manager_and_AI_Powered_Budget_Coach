import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import Transactions from './pages/TransactionsPage';
import GroupsPage from './pages/GroupsPage';
import EventDetailPage from './pages/EventDetailPage';
import Settings from './pages/Settings';
import AiCoachPage from './pages/AiCoachPage'; // Add new import
import AnalyticsPage from './pages/AnalyticsPage'; 
import TransactionsPage from './pages/TransactionsPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/analytics" element={<AnalyticsPage />} />
          <Route path="/transactions" element={<TransactionsPage />} />
          <Route path="/transactions" element={<Transactions />} />
          <Route path="/groups" element={<GroupsPage />} />
          <Route path="/groups/:eventId" element={<EventDetailPage />} /> {/* New dynamic route */}
          <Route path="/ai-coach" element={<AiCoachPage />} /> {/* Add new route */}
          <Route path="/settings" element={<Settings />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;