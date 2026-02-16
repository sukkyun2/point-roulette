
interface SidebarProps {
  onMenuSelect: (menu: string) => void;
  selectedMenu: string;
}

export default function Sidebar({ onMenuSelect, selectedMenu }: SidebarProps) {
  const menuItems = [
    { id: 'dashboard', label: '대시보드', icon: '📊' },
    { id: 'orders', label: '주문 관리', icon: '🛒' },
    { id: 'products', label: '상품 관리', icon: '📦' },
    { id: 'roulette', label: '룰렛 관리', icon: '🎰' },
    { id: 'budget', label: '예산 관리', icon: '💰' },
  ];

  return (
    <div className="w-64 bg-gray-900 text-white min-h-screen flex flex-col">
      <div className="p-6">
        <h1 className="text-xl font-bold">Point Roulette Admin</h1>
      </div>
      
      <nav className="flex-1">
        <ul className="space-y-1 px-4">
          {menuItems.map((item) => (
            <li key={item.id}>
              <button
                onClick={() => onMenuSelect(item.id)}
                className={`w-full flex items-center px-4 py-3 rounded-lg text-left transition-colors ${
                  selectedMenu === item.id
                    ? 'bg-blue-600 text-white'
                    : 'text-gray-300 hover:bg-gray-800 hover:text-white'
                }`}
              >
                <span className="mr-3">{item.icon}</span>
                <span>{item.label}</span>
              </button>
            </li>
          ))}
        </ul>
      </nav>
    </div>
  );
}