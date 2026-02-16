import { useLogin } from '../api/auth-api/auth-api';
import { LoginResult } from '../api/models';

type LoginMutationOptions = {
  onSuccess?: (data: LoginResult) => void;
};

export const useLoginMutation = (options?: LoginMutationOptions) => {
  return useLogin({
    mutation: {
      onSuccess: response => {
        options?.onSuccess?.(response.data);
      },
    },
  });
};
