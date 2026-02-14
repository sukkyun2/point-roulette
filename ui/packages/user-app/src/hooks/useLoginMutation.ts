import { useLogin } from '../api/auth-api/auth-api';

interface LoginResponseData {
  token: string;
  userId: number;
  nickname: string;
}

type LoginMutationOptions = {
  onSuccess?: (data: LoginResponseData | undefined) => void;
};

export const useLoginMutation = (options?: LoginMutationOptions) => {
  return useLogin({
    mutation: {
      onSuccess: (response) => {
        const responseData = response.data as LoginResponseData | undefined;
        options?.onSuccess?.(responseData);
      },
    },
  });
};