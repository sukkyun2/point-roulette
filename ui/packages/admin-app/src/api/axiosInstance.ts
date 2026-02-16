import axios, { AxiosRequestConfig } from 'axios';

const BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

const axiosClient = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Response interceptor for handling errors
axiosClient.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    // Don't handle canceled requests as errors
    if (axios.isCancel(error) || error.name === 'CanceledError') {
      return Promise.reject(error);
    }
    
    if (error.code === 'ECONNREFUSED' || error.code === 'ERR_NETWORK') {
      // Only show alert for actual network connection errors
      console.error('Network connection error:', error);
      alert('서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.');
    } else if (error.response) {
      // Server responded with error status
      console.error('API Error:', error.response.status, error.response.data);
    } else {
      // Other errors (network timeout, etc.)
      console.error('Request Error:', error);
    }
    return Promise.reject(error);
  }
);

export interface CancelablePromise<T> extends Promise<T> {
  cancel: () => void;
}

export const axiosInstance = <T = unknown>(
  config: AxiosRequestConfig,
  options?: AxiosRequestConfig
): CancelablePromise<T> => {
  const source = axios.CancelToken.source();
  const promise = axiosClient({
    ...config,
    ...options,
    cancelToken: source.token,
  }).then(({ data }) => data) as CancelablePromise<T>;

  promise.cancel = () => {
    source.cancel('Query was cancelled');
  };

  return promise;
};

export default axiosInstance;