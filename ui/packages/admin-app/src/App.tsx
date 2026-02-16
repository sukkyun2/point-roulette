import { useState } from 'react';
import Sidebar from './components/Sidebar';
import Dashboard from './components/Dashboard';
import OrderManagement from './components/OrderManagement';
import ProductManagement from './components/ProductManagement';
import BudgetManagement from './components/BudgetManagement';
import RouletteParticipationHistory from './components/RouletteParticipationHistory';

function App() {
  const [selectedMenu, setSelectedMenu] = useState('dashboard');

  const renderContent = () => {
    switch (selectedMenu) {
      case 'dashboard':
        return <Dashboard />;
      case 'orders':
        return <OrderManagement />;
      case 'products':
        return <ProductManagement />;
      case 'roulette':
        return <RouletteParticipationHistory />;
      case 'budget':
        return <BudgetManagement />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="min-h-screen flex bg-gray-100">
      <Sidebar onMenuSelect={setSelectedMenu} selectedMenu={selectedMenu} />
      <main className="flex-1">
        {renderContent()}
      </main>
    </div>
  );
}

export default App;
