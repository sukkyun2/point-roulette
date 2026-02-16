import React, { createContext, useContext, useRef } from 'react';

type RefetchFunction = () => void;

interface RefetchContextType {
  registerRefetch: (page: string, refetch: RefetchFunction) => void;
  refetchPage: (page: string) => void;
}

const RefetchContext = createContext<RefetchContextType | null>(null);

export const RefetchProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const refetchFunctions = useRef<Record<string, RefetchFunction>>({});

  const registerRefetch = (page: string, refetch: RefetchFunction) => {
    refetchFunctions.current[page] = refetch;
  };

  const refetchPage = (page: string) => {
    const refetch = refetchFunctions.current[page];
    if (refetch) {
      refetch();
    }
  };

  return (
    <RefetchContext.Provider value={{ registerRefetch, refetchPage }}>
      {children}
    </RefetchContext.Provider>
  );
};

export const useRefetch = () => {
  const context = useContext(RefetchContext);
  if (!context) {
    throw new Error('useRefetch must be used within RefetchProvider');
  }
  return context;
};