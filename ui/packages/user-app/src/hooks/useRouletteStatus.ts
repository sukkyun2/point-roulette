import { useGetStatus } from '../api/roulette-status-query-api/roulette-status-query-api';
import type { RouletteStatusQueryResponse } from '../api/models';

export function useRouletteStatus() {
  const query = useGetStatus();

  return {
    data: query.data?.data as RouletteStatusQueryResponse | undefined,
    isLoading: query.isLoading,
    isError: query.isError,
    error: query.error,
    refetch: query.refetch,
  };
}