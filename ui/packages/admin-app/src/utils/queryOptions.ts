import type { UseQueryOptions } from '@tanstack/react-query';

export const defaultQueryOptions = {
  staleTime: 5 * 60 * 1000, // 5분
  refetchOnWindowFocus: false,
  retry: 2,
} as const;

export const getQueryOptions = <TData = unknown, TError = unknown>(
  overrides?: Partial<UseQueryOptions<TData, TError>>
): Partial<UseQueryOptions<TData, TError>> => ({
  ...defaultQueryOptions,
  ...overrides,
});