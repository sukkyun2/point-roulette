import { useState } from 'react';
import { Wheel } from 'react-custom-roulette';
import { useRouletteParticipation } from '../hooks/useRouletteParticipation';

interface RouletteWheelProps {
  onResult?: (points: number) => void;
}

export default function RouletteWheel({ onResult }: RouletteWheelProps) {
  const [mustSpin, setMustSpin] = useState(false);
  const [prizeNumber, setPrizeNumber] = useState(0);
  const { participate, isLoading } = useRouletteParticipation();

  // 10개 포인트 섹션 (100p-1000p, 100단위)
  const data = [
    { option: '100p', style: { backgroundColor: '#FF6B6B', textColor: '#ffffff' } },
    { option: '200p', style: { backgroundColor: '#4ECDC4', textColor: '#ffffff' } },
    { option: '300p', style: { backgroundColor: '#45B7D1', textColor: '#ffffff' } },
    { option: '400p', style: { backgroundColor: '#96CEB4', textColor: '#ffffff' } },
    { option: '500p', style: { backgroundColor: '#FFEAA7', textColor: '#333333' } },
    { option: '600p', style: { backgroundColor: '#DDA0DD', textColor: '#ffffff' } },
    { option: '700p', style: { backgroundColor: '#98D8C8', textColor: '#333333' } },
    { option: '800p', style: { backgroundColor: '#FD79A8', textColor: '#ffffff' } },
    { option: '900p', style: { backgroundColor: '#6C5CE7', textColor: '#ffffff' } },
    { option: '1000p', style: { backgroundColor: '#A29BFE', textColor: '#ffffff' } },
  ];

  const pointValues = [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000];

  const handleSpinClick = async () => {
    if (!mustSpin && !isLoading) {
      // API 호출하여 결과 미리 받기
      const result = await participate();
      
      if (result !== null) {
        // 정상적인 경우 - 받은 포인트에 해당하는 인덱스 찾기
        const resultIndex = pointValues.indexOf(result);
        if (resultIndex !== -1) {
          setPrizeNumber(resultIndex);
          setMustSpin(true);
        } else {
          alert('알 수 없는 포인트 값입니다.');
        }
      }
      // 에러인 경우 participate 함수에서 이미 alert 처리됨
    }
  };

  const handleStopSpinning = () => {
    setMustSpin(false);
    // 3초 후에 결과 콜백 호출
    setTimeout(() => {
      const points = pointValues[prizeNumber];
      onResult?.(points);
    }, 100); // 애니메이션 완료 직후
  };

  return (
    <div className="flex flex-col items-center space-y-6">
      {/* 룰렛 휠 */}
      <div className="relative">
        <Wheel
          mustStartSpinning={mustSpin}
          prizeNumber={prizeNumber}
          data={data}
          onStopSpinning={handleStopSpinning}
          backgroundColors={['#3e3e3e', '#df3428']}
          textColors={['#ffffff']}
          fontSize={16}
          outerBorderColor={'#333333'}
          outerBorderWidth={8}
          innerRadius={30}
          innerBorderColor={'#333333'}
          innerBorderWidth={5}
          radiusLineColor={'#333333'}
          radiusLineWidth={2}
          spinDuration={3} // 3초 스핀 지속시간
        />
      </div>

      {/* 스핀 버튼 */}
      <button
        onClick={handleSpinClick}
        disabled={mustSpin || isLoading}
        className={`px-8 py-4 rounded-lg font-bold text-lg transition-all duration-200 ${
          mustSpin || isLoading
            ? 'bg-gray-400 text-gray-600 cursor-not-allowed'
            : 'bg-gradient-to-r from-red-500 to-pink-500 text-white hover:from-red-600 hover:to-pink-600 active:scale-95 shadow-lg'
        }`}
      >
        {isLoading ? '참여 중...' : mustSpin ? '돌리는 중...' : '룰렛 돌리기'}
      </button>
    </div>
  );
}