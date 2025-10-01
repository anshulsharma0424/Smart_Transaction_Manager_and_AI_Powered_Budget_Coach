import axios from 'axios';
import keycloak from '../keycloak';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

// This interceptor is a best practice. It attaches the latest access token
// from Keycloak to the Authorization header of every outgoing API request.
apiClient.interceptors.request.use(
  (config) => {
    if (keycloak.authenticated) {
      config.headers.Authorization = `Bearer ${keycloak.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default apiClient;