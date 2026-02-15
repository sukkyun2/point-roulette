import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import App from './App.tsx';
import './index.css';

const queryClient = new QueryClient();

const AppWithProviders = () => (
  <QueryClientProvider client={queryClient}>
    <App />
  </QueryClientProvider>
);

ReactDOM.createRoot(document.getElementById('root')!).render(
  (import.meta as any).env?.DEV ? (
    // Disable StrictMode in development to prevent double API calls
    <AppWithProviders />
  ) : (
    <React.StrictMode>
      <AppWithProviders />
    </React.StrictMode>
  )
);
