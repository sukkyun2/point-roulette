import { useState } from 'react';
import RouletteWheel from './RouletteWheel';
import { useAuth } from '../contexts/AuthContext';
import { useRouletteStatus } from '../hooks/useRouletteStatus';

export const RoulettePage = () => {
  const [result, setResult] = useState<number | null>(null);
  const { refreshUser } = useAuth();
  const { data: rouletteStatus, isLoading, isError, refetch } = useRouletteStatus();

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-400 via-pink-500 to-red-500 flex items-center justify-center p-4">
        <div className="bg-white rounded-2xl shadow-2xl p-8 max-w-md w-full text-center">
          <div className="text-xl">로딩 중...</div>
        </div>
      </div>
    );
  }

  if (isError) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-400 via-pink-500 to-red-500 flex items-center justify-center p-4">
        <div className="bg-white rounded-2xl shadow-2xl p-8 max-w-md w-full text-center">
          <div className="text-xl text-red-600 mb-4">오류가 발생했습니다</div>
          <button
            onClick={() => refetch()}
            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          >
            다시 시도
          </button>
        </div>
      </div>
    );
  }
  
  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-400 via-pink-500 to-red-500 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-2xl p-8 max-w-4xl w-full">
        <h1 className="text-3xl font-bold text-gray-800 mb-8 text-center">🎰 포인트 룰렛</h1>
        
        <div className="flex flex-col lg:flex-row gap-8 items-center lg:items-start">
          {/* 룰렛 휠 영역 */}
          <div className="flex-1 flex flex-col items-center">
            <div className="relative">
              <RouletteWheel 
                onResult={(points) => {
                  setResult(points);
                  // Refresh user balance and roulette status after game
                  refreshUser();
                  refetch();
                }}
              />
            </div>

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

          {/* 룰렛 상태 정보 */}
          <div className="flex-1 max-w-sm w-full">
            <div className="bg-gray-50 rounded-xl p-6">
              <h2 className="text-xl font-bold text-gray-800 mb-4">오늘의 참여 현황</h2>
              
              <div className="space-y-4">
                {/* 참여 상태 */}
                <div className="flex justify-between items-center">
                  <span className="text-sm font-medium text-gray-600">참여 상태</span>
                  <span className={`px-3 py-1 rounded-full text-sm font-semibold ${
                    rouletteStatus?.hasParticipatedToday 
                      ? 'bg-red-100 text-red-800' 
                      : 'bg-green-100 text-green-800'
                  }`}>
                    {rouletteStatus?.hasParticipatedToday ? '참여 완료' : '참여 가능'}
                  </span>
                </div>

                {/* 남은 예산 */}
                <div className="flex justify-between items-center">
                  <span className="text-sm font-medium text-gray-600">남은 예산</span>
                  <span className="text-lg font-bold text-blue-600">
                    {rouletteStatus?.remainingBudget?.toLocaleString()}원
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};