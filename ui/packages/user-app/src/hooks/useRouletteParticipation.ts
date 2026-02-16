import { useParticipate } from '../api/roulette-participate-api/roulette-participate-api';
import { RouletteParticipateResponse } from '../api/models';

interface RouletteParticipationResult {
  participate: () => Promise<number | null>;
  isLoading: boolean;
}

export const useRouletteParticipation = (): RouletteParticipationResult => {
  const participateMutation = useParticipate();

  const participate = async (): Promise<number | null> => {
      const response = await participateMutation.mutateAsync();
      
      if(response.code === '400'){
        alert(response.message);
        return null;
      }

      const {value: point} = response.data as RouletteParticipateResponse;
      return point;
  };

  return {
    participate,
    isLoading: participateMutation.isPending,
  };
};