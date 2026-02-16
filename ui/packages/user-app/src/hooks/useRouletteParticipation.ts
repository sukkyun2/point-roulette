import { useParticipate } from '../api/roulette-participate-api/roulette-participate-api';
import { RouletteParticipateResponse } from '../api/models';
import { isParticipateResponse, hasErrorCode } from '../utils/typeGuards';

interface RouletteParticipationResult {
  participate: () => Promise<number | null>;
  isLoading: boolean;
}

export const useRouletteParticipation = (): RouletteParticipationResult => {
  const participateMutation = useParticipate();

  const participate = async (): Promise<number | null> => {
    try {
      const response = await participateMutation.mutateAsync();
      
      // 응답 구조 디버깅
      console.log('API Response:', response);
      
      // 응답 구조 처리 - 두 가지 가능한 구조
      // 1. {code: "400", message: "...", data: null}
      // 2. {code: "200", data: {value: ...}}
      
      // 에러 응답인지 확인
      if (hasErrorCode(response, "400") || hasErrorCode(response, 400)) {
        const errorMessage = isParticipateResponse(response) && response.message 
          ? response.message 
          : '참여할 수 없습니다.';
        alert(errorMessage);
        return null;
      }
      
      // 중첩된 에러 구조 확인
      if (isParticipateResponse(response) && response.data && hasErrorCode(response.data, "400")) {
        const nestedErrorMessage = typeof response.data === 'object' && response.data !== null && 'message' in response.data 
          ? String(response.data.message)
          : '참여할 수 없습니다.';
        alert(nestedErrorMessage);
        return null;
      }
      
      // 정상적인 응답에서 포인트 값 추출
      if (isParticipateResponse(response) && response.data) {
        const data = response.data as RouletteParticipateResponse;
        const pointValue = data?.value;
        if (pointValue !== undefined && pointValue !== null) {
          return pointValue;
        }
      }
      
      // 예상치 못한 응답 구조
      console.error('Unexpected response structure:', response);
      alert('알 수 없는 오류가 발생했습니다.');
      return null;
      
    } catch (error) {
      console.error('룰렛 참여 중 오류:', error);
      alert('네트워크 오류가 발생했습니다.');
      return null;
    }
  };

  return {
    participate,
    isLoading: participateMutation.isPending,
  };
};