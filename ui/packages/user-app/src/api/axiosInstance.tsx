import axios, { AxiosRequestConfig } from 'axios';

const BASE_URL =
  (import.meta as any).env.VITE_API_BASE_URL || 'http://localhost:8080';

const axiosClient = axios.create({
  baseURL: BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for adding auth tokens
axiosClient.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

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
    
    if (error.response?.status === 401) {
      // Handle unauthorized access
      localStorage.removeItem('token');
      window.location.reload();
    } else if (error.code === 'ECONNREFUSED' || error.code === 'ERR_NETWORK') {
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

export const axiosInstance = <T = any,>(
  config: AxiosRequestConfig,
  options?: AxiosRequestConfig
): Promise<T> => {
  const source = axios.CancelToken.source();
  const promise = axiosClient({
    ...config,
    ...options,
    cancelToken: source.token,
  }).then(({ data }) => data);

  // @ts-ignore
  promise.cancel = () => {
    source.cancel('Query was cancelled');
  };

  return promise;
};

export default axiosInstance;
