import { AuthProvider, useAuth } from './contexts/AuthContext';
import { LoginForm } from './components/LoginForm';
import { RoulettePage } from './components/RoulettePage';
import { ProductListPage } from './pages/ProductListPage';
import PointHistoryPage from './components/PointHistoryPage';
import OrderHistoryPage from './components/OrderHistoryPage';
import { useState } from 'react';

type Page = 'roulette' | 'products' | 'point-history' | 'order-history';

const Navigation = ({ currentPage, onPageChange }: {
  currentPage: Page;
  onPageChange: (page: Page) => void;
}) => (
  <nav className="fixed bottom-0 left-0 right-0 bg-white shadow-lg border-t border-gray-200 z-50">
    <div className="max-w-4xl mx-auto">
      <div className="grid grid-cols-4">
        <button
          onClick={() => onPageChange('roulette')}
          className={`py-3 px-2 flex flex-col items-center transition-colors ${
            currentPage === 'roulette'
              ? 'text-blue-600'
              : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          <div className="text-xl mb-1">🎰</div>
          <div className="text-xs font-medium">룰렛</div>
        </button>
        <button
          onClick={() => onPageChange('products')}
          className={`py-3 px-2 flex flex-col items-center transition-colors ${
            currentPage === 'products'
              ? 'text-blue-600'
              : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          <div className="text-xl mb-1">🛍️</div>
          <div className="text-xs font-medium">상품</div>
        </button>
        <button
          onClick={() => onPageChange('point-history')}
          className={`py-3 px-2 flex flex-col items-center transition-colors ${
            currentPage === 'point-history'
              ? 'text-blue-600'
              : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          <div className="text-xl mb-1">💰</div>
          <div className="text-xs font-medium">포인트</div>
        </button>
        <button
          onClick={() => onPageChange('order-history')}
          className={`py-3 px-2 flex flex-col items-center transition-colors ${
            currentPage === 'order-history'
              ? 'text-blue-600'
              : 'text-gray-500 hover:text-gray-700'
          }`}
        >
          <div className="text-xl mb-1">📦</div>
          <div className="text-xs font-medium">주문</div>
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
    <div className="min-h-screen bg-gray-50 pb-20">
      {currentPage === 'roulette' && <RoulettePage />}
      {currentPage === 'products' && <ProductListPage />}
      {currentPage === 'point-history' && <PointHistoryPage />}
      {currentPage === 'order-history' && <OrderHistoryPage />}
      <Navigation currentPage={currentPage} onPageChange={handlePageChange} />
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
