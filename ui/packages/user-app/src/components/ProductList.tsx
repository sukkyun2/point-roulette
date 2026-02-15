import React from 'react';
import { useGetProducts } from '../api/product-list-query-api/product-list-query-api';
import { ProductListQueryResponse } from '../api/models';
import { useAuth } from '../contexts/AuthContext';

interface ProductItemProps {
  product: ProductListQueryResponse;
  userBalance: number;
}

const ProductItem: React.FC<ProductItemProps> = ({ product, userBalance }) => {
  const canPurchase = userBalance >= (product.price || 0);

  return (
    <div className="border border-gray-200 rounded-lg p-4 shadow-sm">
      <h3 className="text-lg font-semibold text-gray-800 mb-2">
        {product.name}
      </h3>
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
          {canPurchase ? '구매 가능' : '잔액 부족'}
        </span>
      </div>
    </div>
  );
};

export const ProductList: React.FC = () => {
  const { user } = useAuth();
  const { data, isLoading, error } = useGetProducts();

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
    <div className="max-w-4xl mx-auto p-6">
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
            />
          ))}
        </div>
      )}
    </div>
  );
};