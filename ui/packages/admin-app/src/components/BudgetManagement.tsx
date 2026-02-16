import { useState } from 'react';
import { useGetRouletteBudgetList } from '@/api/roulette-budget-list-query-admin-api/roulette-budget-list-query-admin-api';
import { useCreateRouletteBudget } from '@/api/roulette-budget-create-admin-api/roulette-budget-create-admin-api';
import { useUpdateRouletteBudget } from '@/api/roulette-budget-update-admin-api/roulette-budget-update-admin-api';
import type { Item, RouletteBudgetCreateRequest, RouletteBudgetUpdateRequest } from '@shared/api-models';

const BudgetManagement = () => {
  const [selectedBudget, setSelectedBudget] = useState<Item | null>(null);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
  const [budgetAmount, setBudgetAmount] = useState('');
  const [budgetDate, setBudgetDate] = useState('');

  const { data: budgetData, isLoading, error, refetch } = useGetRouletteBudgetList();
  const createBudgetMutation = useCreateRouletteBudget({
    mutation: {
      onSuccess: () => {
        refetch();
        setIsCreateModalOpen(false);
        setBudgetAmount('');
        setBudgetDate('');
      }
    }
  });
  const updateBudgetMutation = useUpdateRouletteBudget({
    mutation: {
      onSuccess: () => {
        refetch();
        setIsUpdateModalOpen(false);
        setSelectedBudget(null);
        setBudgetAmount('');
        setBudgetDate('');
      }
    }
  });

  const handleCreateBudget = () => {
    if (!budgetAmount || !budgetDate) return;
    
    const createRequest: RouletteBudgetCreateRequest = {
      budgetDate,
      totalBudget: parseInt(budgetAmount)
    };
    
    createBudgetMutation.mutate({ data: createRequest });
  };

  const handleUpdateBudget = () => {
    if (!selectedBudget || !budgetAmount || !budgetDate) return;
    
    const updateRequest: RouletteBudgetUpdateRequest = {
      budgetDate,
      totalBudget: parseInt(budgetAmount)
    };
    
    const budgetId = budgetData?.data.items.findIndex(item => 
      item.budgetDate === selectedBudget.budgetDate
    );
    
    if (budgetId !== undefined && budgetId >= 0) {
      updateBudgetMutation.mutate({ budgetId: budgetId + 1, data: updateRequest });
    }
  };

  const openCreateModal = () => {
    setIsCreateModalOpen(true);
    setBudgetDate(new Date().toISOString().split('T')[0]);
    setBudgetAmount('100000');
  };

  const openUpdateModal = (budget: Item) => {
    setSelectedBudget(budget);
    setIsUpdateModalOpen(true);
    setBudgetDate(budget.budgetDate);
    setBudgetAmount(budget.totalBudget.toString());
  };

  const closeModals = () => {
    setIsCreateModalOpen(false);
    setIsUpdateModalOpen(false);
    setSelectedBudget(null);
    setBudgetAmount('');
    setBudgetDate('');
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('ko-KR').format(amount) + 'p';
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('ko-KR');
  };

  if (isLoading) {
    return (
      <div className="p-6">
        <div className="flex justify-center items-center h-64">
          <div className="text-gray-500">예산 정보를 불러오는 중...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-6">
        <div className="flex justify-center items-center h-64">
          <div className="text-red-500">예산 정보를 불러오는데 실패했습니다.</div>
        </div>
      </div>
    );
  }

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">예산 관리</h1>
        <button
          onClick={openCreateModal}
          className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg font-medium"
        >
          새 예산 설정
        </button>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                날짜
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                총 예산
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                잔여 예산
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                최근 수정
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                작업
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {budgetData?.data.items?.map((budget, index) => (
              <tr key={index} className="hover:bg-gray-50">
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {formatDate(budget.budgetDate)}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {formatCurrency(budget.totalBudget)}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <span className={budget.remainingBudget <= 0 ? 'text-red-600' : 'text-green-600'}>
                    {formatCurrency(budget.remainingBudget)}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {new Date(budget.updatedAt).toLocaleString('ko-KR')}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button
                    onClick={() => openUpdateModal(budget)}
                    className="text-blue-600 hover:text-blue-900 mr-3"
                  >
                    수정
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {budgetData?.data.items?.length === 0 && (
          <div className="text-center py-12">
            <div className="text-gray-500">등록된 예산이 없습니다.</div>
            <button
              onClick={openCreateModal}
              className="mt-4 bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg font-medium"
            >
              첫 번째 예산 설정하기
            </button>
          </div>
        )}
      </div>

      {(isCreateModalOpen || isUpdateModalOpen) && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">
              {isCreateModalOpen ? '새 예산 설정' : '예산 수정'}
            </h2>
            
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  날짜
                </label>
                <input
                  type="date"
                  value={budgetDate}
                  onChange={(e) => setBudgetDate(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  예산 금액 (포인트)
                </label>
                <input
                  type="number"
                  value={budgetAmount}
                  onChange={(e) => setBudgetAmount(e.target.value)}
                  min="0"
                  step="1000"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="100000"
                />
              </div>
            </div>
            
            <div className="flex justify-end space-x-3 mt-6">
              <button
                onClick={closeModals}
                className="px-4 py-2 text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300"
                disabled={createBudgetMutation.isPending || updateBudgetMutation.isPending}
              >
                취소
              </button>
              <button
                onClick={isCreateModalOpen ? handleCreateBudget : handleUpdateBudget}
                className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:opacity-50"
                disabled={createBudgetMutation.isPending || updateBudgetMutation.isPending}
              >
                {createBudgetMutation.isPending || updateBudgetMutation.isPending 
                  ? '처리 중...' 
                  : isCreateModalOpen ? '설정' : '수정'
                }
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default BudgetManagement;