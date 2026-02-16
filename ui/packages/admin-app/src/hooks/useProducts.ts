import { useGetProducts } from '../api/product-list-query-api/product-list-query-api';
import { getQueryOptions } from '../utils/queryOptions';

export const useProducts = () => {
  return useGetProducts({
    query: getQueryOptions(),
  });
};