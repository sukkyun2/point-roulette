import React, { createContext, useContext, useEffect, useState } from 'react';
import { useGetCurrentUser } from '../api/auth-api/auth-api';

interface User {
  id: number;
  nickname: string;
  token: string;
  userBalance: number;
}

interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  setUser: (user: User | null) => void;
  refreshUser: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<User | null>(null);

  const { data, isLoading, error, refetch } = useGetCurrentUser({
    query: {
      enabled: !!localStorage.getItem('token'),
      retry: false,
    },
  });

  useEffect(() => {
    if (
      data?.data &&
      typeof data.data === 'object' &&
      'id' in data.data &&
      'nickname' in data.data &&
      'userBalance' in data.data
    ) {
      setUser(data.data as unknown as User);
    } else if (error) {
      localStorage.removeItem('token');
      setUser(null);
    }
  }, [data, error]);

  const refreshUser = () => {
    if (localStorage.getItem('token')) {
      refetch();
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        isAuthenticated: !!user,
        setUser,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
