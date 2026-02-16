import { useQueryClient } from '@tanstack/react-query';
import { useCreateProduct } from '../api/product-create-admin-api/product-create-admin-api';
import { useUpdateProduct } from '../api/product-update-admin-api/product-update-admin-api';
import { getGetProductsQueryKey } from '../api/product-list-query-api/product-list-query-api';

export const useCreateProductMutation = () => {
  const queryClient = useQueryClient();

  return useCreateProduct({
    mutation: {
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: getGetProductsQueryKey(),
        });
      },
    },
  });
};

export const useUpdateProductMutation = () => {
  const queryClient = useQueryClient();

  return useUpdateProduct({
    mutation: {
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: getGetProductsQueryKey(),
        });
      },
    },
  });
};