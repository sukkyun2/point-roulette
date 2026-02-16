import { AxiosError } from 'axios';
import type { ApiResponse, ApiResponseListProductListQueryResponse, ApiResponseRouletteParticipateResponse } from '../api/models';

export function isAxiosError(error: unknown): error is AxiosError {
  return error instanceof AxiosError;
}

export function isCanceledError(error: unknown): boolean {
  if (!isAxiosError(error)) return false;
  return error.name === 'CanceledError' || error.code === 'ERR_CANCELED';
}

export function isApiResponse(response: unknown): response is ApiResponse {
  return (
    typeof response === 'object' &&
    response !== null &&
    ('code' in response || 'message' in response || 'data' in response)
  );
}

export function isGetProductsResponse(response: unknown): response is ApiResponseListProductListQueryResponse {
  return (
    isApiResponse(response) &&
    typeof response.data !== 'undefined'
  );
}

export function isParticipateResponse(response: unknown): response is ApiResponseRouletteParticipateResponse {
  return (
    isApiResponse(response) &&
    typeof response.data === 'object' &&
    response.data !== null
  );
}

export function hasErrorCode(response: unknown, code: string | number): boolean {
  if (!isApiResponse(response)) return false;
  return response.code === code || response.code === String(code);
}