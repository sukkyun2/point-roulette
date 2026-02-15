import { useParticipate } from '../api/roulette-participate-api/roulette-participate-api';
import { RouletteParticipateResponse } from '../api/models';
import { useAppAlert } from './useAppAlert';

interface RouletteParticipationResult {
  participate: () => Promise<number | null>;
  isLoading: boolean;
}

export const useRouletteParticipation = (): RouletteParticipationResult => {
  const participateMutation = useParticipate();
  const { showAlert } = useAppAlert();

  const participate = async (): Promise<number | null> => {
    try {
      const response = await participateMutation.mutateAsync();
      
      // 응답 구조 디버깅
      console.log('API Response:', response);
      
      // 두 가지 가능한 응답 구조 처리
      // 1. {code: "400", message: "...", data: null}
      // 2. {data: {code: 400, message: "...", value: ...}}
      
      const responseData = response as any;
      const nestedData = (response as any).data as any;
      
      // 첫 번째 구조: response 자체에 code가 있는 경우
      if (responseData?.code === "400" || responseData?.code === 400) {
        showAlert(responseData.message || '참여할 수 없습니다.');
        return null;
      }
      
      // 두 번째 구조: response.data에 code가 있는 경우
      if (nestedData?.code === "400" || nestedData?.code === 400) {
        showAlert(nestedData.message || '참여할 수 없습니다.');
        return null;
      }
      
      // 정상적인 경우 - 포인트 값 반환
      // 먼저 nestedData에서 value 확인
      const pointValue = nestedData?.value ?? (responseData as RouletteParticipateResponse)?.value;
      if (pointValue !== undefined && pointValue !== null) {
        return pointValue;
      }
      
      // 예상치 못한 응답 구조
      console.error('Unexpected response structure:', response);
      showAlert('알 수 없는 오류가 발생했습니다.');
      return null;
      
    } catch (error) {
      console.error('룰렛 참여 중 오류:', error);
      showAlert('네트워크 오류가 발생했습니다.');
      return null;
    }
  };

  return {
    participate,
    isLoading: participateMutation.isPending,
  };
};