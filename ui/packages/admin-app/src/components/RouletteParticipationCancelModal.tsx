interface RouletteParticipationCancelModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  participation: {
    id: number;
    nickName: string;
    earnPoint: number;
    createdAt: string;
  } | null;
  isLoading: boolean;
}

export default function RouletteParticipationCancelModal({
  isOpen,
  onClose,
  onConfirm,
  participation,
  isLoading
}: RouletteParticipationCancelModalProps) {
  if (!isOpen || !participation) return null;

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

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
        {/* 배경 오버레이 */}
        <div 
          className="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75"
          onClick={onClose}
        ></div>

        {/* 모달 컨테이너 */}
        <span className="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        
        <div className="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <div className="sm:flex sm:items-start">
              <div className="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
                <span className="text-red-600 text-xl">⚠️</span>
              </div>
              <div className="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                <h3 className="text-lg leading-6 font-medium text-gray-900">
                  룰렛 참여 취소
                </h3>
                <div className="mt-2">
                  <p className="text-sm text-gray-500 mb-4">
                    다음 룰렛 참여를 취소하시겠습니까? 이 작업은 되돌릴 수 없습니다.
                  </p>
                  
                  <div className="bg-gray-50 rounded-lg p-4 space-y-2">
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium text-gray-700">참여 ID:</span>
                      <span className="text-sm text-gray-900">#{participation.id}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium text-gray-700">사용자:</span>
                      <span className="text-sm text-gray-900">{participation.nickName}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium text-gray-700">지급 포인트:</span>
                      <span className="text-sm font-semibold text-red-600">
                        {formatPoints(participation.earnPoint)}
                      </span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium text-gray-700">참여 일시:</span>
                      <span className="text-sm text-gray-900">
                        {formatDate(participation.createdAt)}
                      </span>
                    </div>
                  </div>

                  <div className="mt-4 p-3 bg-yellow-50 border border-yellow-200 rounded-lg">
                    <div className="flex items-start">
                      <span className="text-yellow-600 mr-2">💡</span>
                      <div className="text-sm text-yellow-800">
                        <p className="font-medium mb-1">취소 시 처리 내용:</p>
                        <ul className="list-disc list-inside space-y-1 text-xs">
                          <li>지급된 포인트가 사용자 잔액에서 차감됩니다</li>
                          <li>잔액이 0이어도 차감 가능합니다 (음수 허용)</li>
                          <li>회수 금액은 해당 일자 예산에 재반영됩니다</li>
                          <li>포인트 히스토리에 회수 기록이 남습니다</li>
                        </ul>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div className="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
            <button
              type="button"
              disabled={isLoading}
              onClick={onConfirm}
              className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? (
                <>
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                  취소 중...
                </>
              ) : (
                '참여 취소'
              )}
            </button>
            <button
              type="button"
              disabled={isLoading}
              onClick={onClose}
              className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
            >
              닫기
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}