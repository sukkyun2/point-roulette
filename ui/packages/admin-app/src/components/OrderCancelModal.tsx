import { OrderItemResponse } from '@shared/api-models';

interface OrderCancelModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  order: OrderItemResponse | null;
  isLoading?: boolean;
}

export default function OrderCancelModal({
  isOpen,
  onClose,
  onConfirm,
  order,
  isLoading = false
}: OrderCancelModalProps) {
  if (!isOpen || !order) return null;

  const formatPoints = (points: number) => {
    return points.toLocaleString() + 'P';
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

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        {/* 배경 오버레이 */}
        <div
          className="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75"
          onClick={onClose}
        />

        {/* 모달 컨테이너 */}
        <div className="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
          {/* 모달 헤더 */}
          <div className="sm:flex sm:items-start">
            <div className="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
              <span className="text-xl">⚠️</span>
            </div>
            <div className="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
              <h3 className="text-lg leading-6 font-medium text-gray-900">
                주문 취소 확인
              </h3>
              <div className="mt-4">
                <p className="text-sm text-gray-500 mb-4">
                  다음 주문을 취소하시겠습니까? 차감된 포인트는 자동으로 환불됩니다.
                </p>
                
                {/* 주문 정보 */}
                <div className="bg-gray-50 rounded-lg p-4 space-y-3">
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-gray-600">주문 ID:</span>
                    <span className="text-sm text-gray-900">#{order.id}</span>
                  </div>
                  
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-gray-600">사용자:</span>
                    <span className="text-sm text-gray-900">{order.nickName}</span>
                  </div>
                  
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-gray-600">상품명:</span>
                    <span className="text-sm text-gray-900">{order.productName}</span>
                  </div>
                  
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-gray-600">차감 포인트:</span>
                    <span className="text-sm font-bold text-red-600">
                      {formatPoints(order.deductedPoints)}
                    </span>
                  </div>
                  
                  <div className="flex justify-between items-center">
                    <span className="text-sm font-medium text-gray-600">주문 일시:</span>
                    <span className="text-sm text-gray-900">{formatDate(order.createdAt)}</span>
                  </div>
                </div>

                {/* 환불 안내 */}
                <div className="mt-4 p-3 bg-blue-50 rounded-lg">
                  <div className="flex items-center">
                    <span className="text-sm mr-2">💰</span>
                    <div>
                      <p className="text-sm font-medium text-blue-800">포인트 환불 안내</p>
                      <p className="text-xs text-blue-600 mt-1">
                        차감된 {formatPoints(order.deductedPoints)}가 사용자에게 즉시 환불됩니다.
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* 모달 버튼 */}
          <div className="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
            <button
              type="button"
              onClick={onConfirm}
              disabled={isLoading}
              className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              {isLoading ? (
                <div className="flex items-center">
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                  처리 중...
                </div>
              ) : (
                '주문 취소'
              )}
            </button>
            <button
              type="button"
              onClick={onClose}
              disabled={isLoading}
              className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              취소
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}