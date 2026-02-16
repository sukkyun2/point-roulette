import { useState } from 'react';
import { useGetParticipationHistories } from '../api/roulette-history-list-query-admin-api/roulette-history-list-query-admin-api';
import { useCancelRouletteParticipation } from '../api/roulette-cancel-admin-api/roulette-cancel-admin-api';
import { RouletteParticipationHistoryItemStatus, GetParticipationHistoriesStatus } from '@shared/api-models';
import RouletteParticipationCancelModal from './RouletteParticipationCancelModal';

export default function RouletteParticipationHistory() {
  const [statusFilter, setStatusFilter] = useState<GetParticipationHistoriesStatus | null>(null);
  const [selectedParticipationId, setSelectedParticipationId] = useState<number | null>(null);
  const [showCancelModal, setShowCancelModal] = useState(false);

  const { data: participationsData, isLoading, error, refetch } = useGetParticipationHistories({
    status: statusFilter || undefined
  });

  const cancelParticipationMutation = useCancelRouletteParticipation({
    mutation: {
      onSuccess: () => {
        refetch();
        setShowCancelModal(false);
        setSelectedParticipationId(null);
      }
    }
  });

  const handleCancelClick = (participationId: number) => {
    setSelectedParticipationId(participationId);
    setShowCancelModal(true);
  };

  const handleConfirmCancel = () => {
    if (selectedParticipationId) {
      cancelParticipationMutation.mutate({ rouletteHistoryId: selectedParticipationId });
    }
  };

  const getStatusBadge = (status: RouletteParticipationHistoryItemStatus) => {
    const baseClasses = "px-2 py-1 rounded-full text-xs font-medium";
    if (status === RouletteParticipationHistoryItemStatus.SUCCESS) {
      return `${baseClasses} bg-green-100 text-green-800`;
    } else if (status === RouletteParticipationHistoryItemStatus.CANCELED) {
      return `${baseClasses} bg-red-100 text-red-800`;
    }
    return `${baseClasses} bg-gray-100 text-gray-800`;
  };

  const getStatusText = (status: RouletteParticipationHistoryItemStatus) => {
    switch (status) {
      case RouletteParticipationHistoryItemStatus.SUCCESS:
        return '지급완료';
      case RouletteParticipationHistoryItemStatus.CANCELED:
        return '취소됨';
      default:
        return status;
    }
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

  const formatPoints = (points: number) => {
    return points.toLocaleString() + 'P';
  };

  const selectedParticipation = selectedParticipationId 
    ? participationsData?.data?.participations?.find(p => p.id === selectedParticipationId) ?? null
    : null;

  if (isLoading) {
    return (
      <div className="p-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">룰렛 참여 내역</h1>
        <div className="bg-white rounded-lg shadow-md p-12">
          <div className="flex justify-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">룰렛 참여 내역</h1>
        <div className="bg-white rounded-lg shadow-md p-12">
          <div className="text-center">
            <span className="text-6xl mb-4 block">⚠️</span>
            <h2 className="text-xl font-semibold text-red-600 mb-2">데이터를 불러올 수 없습니다</h2>
            <p className="text-gray-500 mb-4">
              룰렛 참여 내역을 가져오는 중 오류가 발생했습니다.
            </p>
            <button
              onClick={() => refetch()}
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              다시 시도
            </button>
          </div>
        </div>
      </div>
    );
  }

  const participations = participationsData?.data?.participations || [];

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">룰렛 참여 내역</h1>

      {/* 필터 영역 */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <div className="flex items-center space-x-4">
          <span className="text-sm font-medium text-gray-700">상태 필터:</span>
          <div className="flex space-x-2">
            <button
              onClick={() => setStatusFilter(null)}
              className={`px-3 py-1 rounded-md text-sm font-medium transition-colors ${
                statusFilter === null
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              전체
            </button>
            <button
              onClick={() => setStatusFilter(GetParticipationHistoriesStatus.SUCCESS)}
              className={`px-3 py-1 rounded-md text-sm font-medium transition-colors ${
                statusFilter === GetParticipationHistoriesStatus.SUCCESS
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              지급완료
            </button>
            <button
              onClick={() => setStatusFilter(GetParticipationHistoriesStatus.CANCELED)}
              className={`px-3 py-1 rounded-md text-sm font-medium transition-colors ${
                statusFilter === GetParticipationHistoriesStatus.CANCELED
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              }`}
            >
              취소됨
            </button>
          </div>
          <div className="flex-1 text-right">
            <span className="text-sm text-gray-500">
              총 {participations.length}건의 참여 내역
            </span>
          </div>
        </div>
      </div>

      {/* 참여 내역 테이블 */}
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        {participations.length === 0 ? (
          <div className="p-12 text-center">
            <span className="text-6xl mb-4 block">🎰</span>
            <h2 className="text-xl font-semibold text-gray-700 mb-2">참여 내역이 없습니다</h2>
            <p className="text-gray-500">
              {statusFilter ? '해당 상태의 참여 내역이 없습니다.' : '등록된 참여 내역이 없습니다.'}
            </p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    참여 ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    사용자
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    지급 포인트
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    참여 일시
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    상태
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    작업
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {participations.map((participation) => (
                  <tr key={participation.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      #{participation.id}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {participation.nickName}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatPoints(participation.earnPoint)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatDate(participation.createdAt)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={getStatusBadge(participation.status)}>
                        {getStatusText(participation.status)}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      {participation.status === RouletteParticipationHistoryItemStatus.SUCCESS && (
                        <button
                          onClick={() => handleCancelClick(participation.id)}
                          disabled={cancelParticipationMutation.isPending}
                          className="text-red-600 hover:text-red-900 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        >
                          취소
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* 취소 확인 모달 */}
      <RouletteParticipationCancelModal
        isOpen={showCancelModal}
        onClose={() => {
          setShowCancelModal(false);
          setSelectedParticipationId(null);
        }}
        onConfirm={handleConfirmCancel}
        participation={selectedParticipation}
        isLoading={cancelParticipationMutation.isPending}
      />
    </div>
  );
}