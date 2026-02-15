import React from 'react';
import { useGetProducts } from '../api/product-list-query-api/product-list-query-api';
import { ProductListQueryResponse } from '../api/models';
import { useAuth } from '../contexts/AuthContext';
import { useCreateOrder } from '../api/order-api/order-api';

interface ProductItemProps {
  product: ProductListQueryResponse;
  userBalance: number;
  onPurchase: (productId: string) => void;
  isPurchasing: boolean;
}

const ProductItem: React.FC<ProductItemProps> = ({ 
  product, 
  userBalance, 
  onPurchase, 
  isPurchasing 
}) => {
  const canPurchase = userBalance >= (product.price || 0) && userBalance > 0;

  const handlePurchase = () => {
    if (canPurchase && product.id) {
      onPurchase(product.id);
    }
  };

  return (
    <div className="border border-gray-200 rounded-lg p-4 shadow-sm">
      <h3 className="text-lg font-semibold text-gray-800 mb-2">
        {product.name}
      </h3>
      <div className="space-y-3">
        <div className="flex justify-between items-center">
          <span className="text-xl font-bold text-blue-600">
            {product.price?.toLocaleString()}원
          </span>
          <span
            className={`px-2 py-1 rounded text-sm ${
              canPurchase
                ? 'bg-green-100 text-green-800'
                : 'bg-red-100 text-red-800'
            }`}
          >
            {canPurchase ? '구매 가능' : userBalance < 0 ? '음수 잔액' : '잔액 부족'}
          </span>
        </div>
        <button
          onClick={handlePurchase}
          disabled={!canPurchase || isPurchasing}
          className={`w-full py-2 px-4 rounded font-medium transition-colors ${
            canPurchase && !isPurchasing
              ? 'bg-blue-600 text-white hover:bg-blue-700'
              : 'bg-gray-300 text-gray-500 cursor-not-allowed'
          }`}
        >
          {canPurchase ? '구매하기' : '구매 불가'}
        </button>
      </div>
    </div>
  );
};

export const ProductList: React.FC = () => {
  const { user } = useAuth();
  const { data, isLoading, error } = useGetProducts();
  const createOrderMutation = useCreateOrder();

  const handlePurchase = async (productId: string) => {
    try {
      await createOrderMutation.mutateAsync({ data: { productId } });
      alert('주문이 완료되었습니다!');
    } catch (error) {
      console.error('주문 실패:', error);
      alert('주문에 실패했습니다. 다시 시도해주세요.');
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center py-8">
        <div className="text-lg">상품 목록을 불러오는 중...</div>
      </div>
    );
  }

  if (error) {
    // Don't show error for canceled requests
    if ((error as any).name === 'CanceledError' || (error as any).code === 'ERR_CANCELED') {
      return (
        <div className="flex justify-center items-center py-8">
          <div className="text-gray-500">상품 목록을 불러오는 중...</div>
        </div>
      );
    }
    
    return (
      <div className="flex justify-center items-center py-8">
        <div className="text-red-600">상품 목록을 불러오는데 실패했습니다.</div>
      </div>
    );
  }

  // Handle the data structure - it should be an ApiResponse with data array
  const responseData = data as any;
  const products = Array.isArray(responseData?.data) ? responseData.data : [];
  const userBalance = user?.userBalance || 0;

  return (
    <div className="relative max-w-4xl mx-auto p-6">
      {createOrderMutation.isPending && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg flex items-center space-x-3">
            <svg
              className="animate-spin h-8 w-8 text-blue-600"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle
                className="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                strokeWidth="4"
              ></circle>
              <path
                className="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
              ></path>
            </svg>
            <span className="text-lg font-medium text-gray-700">주문 처리 중...</span>
          </div>
        </div>
      )}
      
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-800 mb-2">상품 목록</h1>
        <div className="text-gray-600">
          보유 잔액:{' '}
          <span className="font-semibold text-blue-600">
            {userBalance.toLocaleString()}원
          </span>
        </div>
      </div>

      {products.length === 0 ? (
        <div className="text-center py-12">
          <div className="text-gray-500 text-lg">등록된 상품이 없습니다.</div>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {products.map((product: ProductListQueryResponse) => (
            <ProductItem
              key={product.id}
              product={product}
              userBalance={userBalance}
              onPurchase={handlePurchase}
              isPurchasing={createOrderMutation.isPending}
            />
          ))}
        </div>
      )}
    </div>
  );
};