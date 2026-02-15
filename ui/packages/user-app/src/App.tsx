import { AuthProvider, useAuth } from './contexts/AuthContext';
import { LoginForm } from './components/LoginForm';
import { RoulettePage } from './components/RoulettePage';
import { ProductListPage } from './pages/ProductListPage';
import { useState } from 'react';

type Page = 'roulette' | 'products';

const Navigation = ({ currentPage, onPageChange }: {
  currentPage: Page;
  onPageChange: (page: Page) => void;
}) => (
  <nav className="bg-white shadow-sm border-b border-gray-200">
    <div className="max-w-4xl mx-auto px-4">
      <div className="flex space-x-8">
        <button
          onClick={() => onPageChange('roulette')}
          className={`py-4 px-2 border-b-2 transition-colors ${
            currentPage === 'roulette'
              ? 'border-blue-500 text-blue-600 font-medium'
              : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
          }`}
        >
          룰렛 게임
        </button>
        <button
          onClick={() => onPageChange('products')}
          className={`py-4 px-2 border-b-2 transition-colors ${
            currentPage === 'products'
              ? 'border-blue-500 text-blue-600 font-medium'
              : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
          }`}
        >
          상품 목록
        </button>
      </div>
    </div>
  </nav>
);

const AppContent = () => {
  const { isAuthenticated, isLoading, refreshUser } = useAuth();
  const [currentPage, setCurrentPage] = useState<Page>('roulette');

  const handlePageChange = (page: Page) => {
    setCurrentPage(page);
    // Refresh user data when switching to products page to get updated balance
    if (page === 'products') {
      refreshUser();
    }
  };

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-lg">로딩 중...</div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <LoginForm />;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation currentPage={currentPage} onPageChange={handlePageChange} />
      {currentPage === 'roulette' && <RoulettePage />}
      {currentPage === 'products' && <ProductListPage />}
    </div>
  );
};

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;
