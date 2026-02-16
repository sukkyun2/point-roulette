import { useEffect } from 'react';
import { useGetOrderHistory } from '../api/order-history-query-api/order-history-query-api';
import { useRefetch } from '../contexts/RefetchContext';
import { OrderHistoryItemResponse, OrderHistoryItemResponseStatus } from '../api/models';

const OrderHistoryPage = () => {
  const { registerRefetch } = useRefetch();
  const { data, isLoading, error, refetch } = useGetOrderHistory();

  useEffect(() => {
    registerRefetch('order-history', refetch);
  }, [registerRefetch, refetch]);

  if (isLoading) {
    return (
      <div className="max-w-4xl mx-auto p-6">
        <div className="text-center py-8">
          <div className="text-lg">주문 내역을 불러오는 중...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-4xl mx-auto p-6">
        <div className="text-center py-8">
          <div className="text-lg text-red-600">주문 내역을 불러올 수 없습니다.</div>
        </div>
      </div>
    );
  }

  const orders = data?.data?.orders || [];

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  const formatTime = (dateString: string) => {
    return new Date(dateString).toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const getStatusLabel = (status: OrderHistoryItemResponseStatus) => {
    switch (status) {
      case OrderHistoryItemResponseStatus.COMPLETED:
        return '주문 완료';
      case OrderHistoryItemResponseStatus.CANCELLED:
        return '주문 취소';
      default:
        return '알 수 없음';
    }
  };

  const getStatusColor = (status: OrderHistoryItemResponseStatus) => {
    switch (status) {
      case OrderHistoryItemResponseStatus.COMPLETED:
        return 'text-green-600 bg-green-50';
      case OrderHistoryItemResponseStatus.CANCELLED:
        return 'text-red-600 bg-red-50';
      default:
        return 'text-gray-600 bg-gray-50';
    }
  };

  return (
    <div className="max-w-4xl mx-auto flex flex-col h-full">
      <div className="p-6 pb-4">
        <h1 className="text-2xl font-bold">주문 내역</h1>
      </div>
      
      {orders.length === 0 ? (
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center text-gray-500">
            주문 내역이 없습니다.
          </div>
        </div>
      ) : (
        <div className="flex-1 overflow-y-auto">
          <div className="divide-y divide-gray-200 bg-white">
            {orders.map((order: OrderHistoryItemResponse) => (
              <div key={order.id} className="p-6 hover:bg-gray-50">
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <h3 className="text-lg font-semibold text-gray-900">
                        {order.productName}
                      </h3>
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status!)}`}>
                        {getStatusLabel(order.status!)}
                      </span>
                    </div>
                    
                    <div className="flex items-center gap-4 mb-2">
                      <div className="text-lg font-bold text-red-600">
                        -{order.deductedPoints?.toLocaleString()}P
                      </div>
                    </div>
                    
                    <div className="text-sm text-gray-600">
                      주문일: {formatDate(order.createdAt!)} {formatTime(order.createdAt!)}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default OrderHistoryPage;