import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import keycloak from '../keycloak';
import { LayoutDashboard, ArrowLeftRight, Users, Bot, PieChart, LogOut } from 'lucide-react';
// REMOVED: ToastNotification import is no longer here.

const Layout = () => {
    return (
        <div className="flex h-screen bg-[#EEEEEE]">
            {/* Sidebar */}
            <aside className="w-64 flex-shrink-0 bg-[#3E3F29] text-white flex flex-col">
                <div className="text-2xl font-bold p-5 border-b border-gray-700 text-center">
                    Spenzr
                </div>
                <nav className="flex-grow p-2">
                    <NavLink to="/" className={({ isActive }) => `flex items-center px-4 py-3 my-1 rounded-md text-sm font-medium hover:bg-[#57564F] ${isActive ? 'bg-[#57564F]' : ''}`}>
                        <LayoutDashboard className="mr-3 h-5 w-5" /> Dashboard
                    </NavLink>
                    <NavLink to="/transactions" className={({ isActive }) => `flex items-center px-4 py-3 my-1 rounded-md text-sm font-medium hover:bg-[#57564F] ${isActive ? 'bg-[#57564F]' : ''}`}>
                        <ArrowLeftRight className="mr-3 h-5 w-5" /> Transactions
                    </NavLink>
                                        <NavLink to="/analytics" className={({ isActive }) => `flex items-center px-4 py-3 my-1 rounded-md text-sm font-medium hover:bg-[#57564F] ${isActive ? 'bg-[#57564F]' : ''}`}>
                        <PieChart className="mr-3 h-5 w-5" /> Analytics
                    </NavLink>
                                        <NavLink to="/ai-coach" className={({ isActive }) => `flex items-center px-4 py-3 my-1 rounded-md text-sm font-medium hover:bg-[#57564F] ${isActive ? 'bg-[#57564F]' : ''}`}>
                        <Bot className="mr-3 h-5 w-5" /> AI Coach
                    </NavLink>
                    <NavLink to="/groups" className={({ isActive }) => `flex items-center px-4 py-3 my-1 rounded-md text-sm font-medium hover:bg-[#57564F] ${isActive ? 'bg-[#57564F]' : ''}`}>
                        <Users className="mr-3 h-5 w-5" /> Groups
                    </NavLink>

                </nav>
                <div className="p-2 border-t border-gray-700">
                    <button
                        onClick={() => keycloak.logout()}
                        className="
      w-full flex items-center gap-2
      px-4 py-3 my-2 rounded-xl text-sm font-semibold
      text-red-400 bg-red-900/20
      hover:bg-[#FF714B] hover:text-white
      transition-all duration-200 ease-in-out
      focus:outline-none focus:ring-2 focus:ring-[#FF714B] focus:ring-offset-1
      active:scale-95
    "
                    >
                        <LogOut className="h-5 w-5" />
                        Logout
                    </button>
                </div>

            </aside>

            {/* Main Content */}
            <main className="flex-1 p-6 lg:p-10 overflow-y-auto relative">
                {/* REMOVED: <ToastNotification /> is no longer here. */}
                <Outlet />
            </main>
        </div>
    );
};

export default Layout;