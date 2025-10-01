import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import keycloak from './keycloak.js';

const root = ReactDOM.createRoot(document.getElementById('root'));

// 1. Render an initial "Loading..." message
root.render(
  <React.StrictMode>
    <div className="flex items-center justify-center h-screen bg-gray-100">
      <p className="text-xl text-gray-600">Initializing Spenzr...</p>
    </div>
  </React.StrictMode>
);

// 2. Initialize Keycloak. 'login-required' forces authentication before the app loads.
keycloak.init({ onLoad: 'login-required' }).then((authenticated) => {
  if (authenticated) {
    // 3. If login is successful, render the main App
    root.render(
      <React.StrictMode>
        <App />
      </React.StrictMode>
    );
  } else {
    // 4. If login fails, show an error message
    root.render(
      <React.StrictMode>
        <div className="flex items-center justify-center h-screen bg-red-100">
          <p className="text-xl text-red-700">Authentication Failed!</p>
        </div>
      </React.StrictMode>
    );
  }
});