import { useGetDashboard } from '../api/dashboard-query-admin-api/dashboard-query-admin-api';

export const useDashboard = () => {
  const { data, isLoading, error, refetch } = useGetDashboard();
  
  return {
    dashboardData: data?.data,
    isLoading,
    error,
    refetch,
  };
};