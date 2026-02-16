import { useState } from 'react';
import { useGetOrderList } from '../api/order-list-query-admin-api/order-list-query-admin-api';
import { useCancelOrder } from '../api/order-cancel-admin-api/order-cancel-admin-api';
import { GetOrderListStatus, OrderItemResponseStatus } from '@shared/api-models';
import OrderCancelModal from './OrderCancelModal';

export default function OrderManagement() {
  const [statusFilter, setStatusFilter] = useState<GetOrderListStatus | null>(null);
  const [selectedOrderId, setSelectedOrderId] = useState<number | null>(null);
  const [showCancelModal, setShowCancelModal] = useState(false);

  const { data: ordersData, isLoading, error, refetch } = useGetOrderList({
    status: statusFilter || undefined
  });

  const cancelOrderMutation = useCancelOrder({
    mutation: {
      onSuccess: () => {
        refetch();
        setShowCancelModal(false);
        setSelectedOrderId(null);
      }
    }
  });

  const handleCancelClick = (orderId: number) => {
    setSelectedOrderId(orderId);
    setShowCancelModal(true);
  };

  const handleConfirmCancel = () => {
    if (selectedOrderId) {
      cancelOrderMutation.mutate({ orderId: selectedOrderId });
    }
  };

  const getStatusBadge = (status: OrderItemResponseStatus) => {
    const baseClasses = "px-2 py-1 rounded-full text-xs font-medium";
    if (status === OrderItemResponseStatus.COMPLETED) {
      return `${baseClasses} bg-green-100 text-green-800`;
    } else if (status === OrderItemResponseStatus.CANCELLED) {
      return `${baseClasses} bg-red-100 text-red-800`;
    }
    return `${baseClasses} bg-gray-100 text-gray-800`;
  };

  const getStatusText = (status: OrderItemResponseStatus) => {
    switch (status) {
      case OrderItemResponseStatus.COMPLETED:
        return '완료';
      case OrderItemResponseStatus.CANCELLED:
        return '취소';
      default:
        return status;
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const formatPoints = (points: number) => {
    return points.toLocaleString() + 'P';
  };

  const selectedOrder = selectedOrderId 
    ? ordersData?.data?.orders?.find(order => order.id === selectedOrderId) ?? null
    : null;

  if (isLoading) {
    return (
      <div className="p-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">주문 관리</h1>
        <div className="bg-white rounded-lg shadow-md p-12">
          <div className="flex justify-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">주문 관리</h1>
        <div className="bg-white rounded-lg shadow-md p-12">
          <div className="text-center">
            <span className="text-6xl mb-4 block">⚠️</span>
            <h2 className="text-xl font-semibold text-red-600 mb-2">데이터를 불러올 수 없습니다</h2>
            <p className="text-gray-500 mb-4">
              주문 정보를 가져오는 중 오류가 발생했습니다.
            </p>
            <button
              onClick={() => refetch()}
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              다시 시도
            </button>
          </div>
        </div>
      </div>
    );
  }

  const orders = ordersData?.data?.orders || [];

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">주문 관리</h1>

      {/* 필터 영역 */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <div className="flex items-center space-x-4">
          <span className="text-sm font-medium text-gray-700">상태 필터:</span>
          <div className="flex space-x-2">
            <button
              onClick={() => setStatusFilter(null)}
              className={`px-3 py-1 rounded-md text-sm font-medium transition-colors ${
                statusFilter === null
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              전체
            </button>
            <button
              onClick={() => setStatusFilter(GetOrderListStatus.COMPLETED)}
              className={`px-3 py-1 rounded-md text-sm font-medium transition-colors ${
                statusFilter === GetOrderListStatus.COMPLETED
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              완료
            </button>
            <button
              onClick={() => setStatusFilter(GetOrderListStatus.CANCELLED)}
              className={`px-3 py-1 rounded-md text-sm font-medium transition-colors ${
                statusFilter === GetOrderListStatus.CANCELLED
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              취소
            </button>
          </div>
          <div className="flex-1 text-right">
            <span className="text-sm text-gray-500">
              총 {orders.length}건의 주문
            </span>
          </div>
        </div>
      </div>

      {/* 주문 테이블 */}
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        {orders.length === 0 ? (
          <div className="p-12 text-center">
            <span className="text-6xl mb-4 block">📋</span>
            <h2 className="text-xl font-semibold text-gray-700 mb-2">주문이 없습니다</h2>
            <p className="text-gray-500">
              {statusFilter ? '해당 상태의 주문이 없습니다.' : '등록된 주문이 없습니다.'}
            </p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    주문 ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    사용자
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    상품명
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    차감 포인트
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    주문 일시
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    상태
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    작업
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {orders.map((order) => (
                  <tr key={order.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      #{order.id}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {order.nickName}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {order.productName}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatPoints(order.deductedPoints)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatDate(order.createdAt)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={getStatusBadge(order.status)}>
                        {getStatusText(order.status)}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      {order.status === OrderItemResponseStatus.COMPLETED && (
                        <button
                          onClick={() => handleCancelClick(order.id)}
                          disabled={cancelOrderMutation.isPending}
                          className="text-red-600 hover:text-red-900 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        >
                          취소
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* 취소 확인 모달 */}
      <OrderCancelModal
        isOpen={showCancelModal}
        onClose={() => {
          setShowCancelModal(false);
          setSelectedOrderId(null);
        }}
        onConfirm={handleConfirmCancel}
        order={selectedOrder}
        isLoading={cancelOrderMutation.isPending}
      />
    </div>
  );
}