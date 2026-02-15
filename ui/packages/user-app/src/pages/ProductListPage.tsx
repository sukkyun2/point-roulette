import React from 'react';
import { ProductList } from '../components/ProductList';

export const ProductListPage: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-50">
      <ProductList />
    </div>
  );
};