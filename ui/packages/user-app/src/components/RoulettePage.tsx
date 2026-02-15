import { useState } from 'react';
import RouletteWheel from './RouletteWheel';

export const RoulettePage = () => {
  const [result, setResult] = useState<number | null>(null);

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-400 via-pink-500 to-red-500 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-2xl p-8 max-w-md w-full text-center">
        <h1 className="text-3xl font-bold text-gray-800 mb-8">🎰 포인트 룰렛</h1>
        
        <RouletteWheel 
          onResult={(points) => {
            setResult(points);
          }}
        />

        {/* 결과 표시 */}
        {result && (
          <div className="mt-6 text-center">
            <div className="text-2xl font-bold text-green-600 mb-2">
              🎉 당첨! 🎉
            </div>
            <div className="text-xl font-semibold">
              {result}포인트 획득!
            </div>
          </div>
        )}
      </div>
    </div>
  );
};