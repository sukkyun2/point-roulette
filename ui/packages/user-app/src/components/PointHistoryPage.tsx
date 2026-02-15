import { useGetPointHistory } from '../api/point-history-query-api/point-history-query-api';
import { PointHistoryItemResponse, PointHistoryItemResponseType } from '../api/models';

const PointHistoryPage = () => {
  const { data, isLoading, error } = useGetPointHistory();

  if (isLoading) {
    return (
      <div className="max-w-4xl mx-auto p-6">
        <div className="text-center py-8">
          <div className="text-lg">포인트 내역을 불러오는 중...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-4xl mx-auto p-6">
        <div className="text-center py-8">
          <div className="text-lg text-red-600">포인트 내역을 불러올 수 없습니다.</div>
        </div>
      </div>
    );
  }

  const balance = data?.data?.balance;
  const history = data?.data?.history || [];

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  const formatTime = (dateString: string) => {
    return new Date(dateString).toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const getTypeLabel = (type: PointHistoryItemResponseType) => {
    switch (type) {
      case 'EARN':
        return '적립';
      case 'USE':
        return '사용';
      default:
        return '알 수 없음';
    }
  };

  const getTypeColor = (type: PointHistoryItemResponseType) => {
    switch (type) {
      case 'EARN':
        return 'text-green-600';
      case 'USE':
        return 'text-red-600';
      default:
        return 'text-gray-600';
    }
  };

  const isExpired = (expiresAt: string | undefined) => {
    if (!expiresAt) return false;
    return new Date(expiresAt) < new Date();
  };

  const isExpiringSoon = (expiresAt: string | undefined) => {
    if (!expiresAt) return false;
    const expireDate = new Date(expiresAt);
    const now = new Date();
    const sevenDaysFromNow = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000);
    return expireDate <= sevenDaysFromNow && expireDate >= now;
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <h1 className="text-2xl font-bold mb-6">포인트 내역</h1>
      
      {balance && (
        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
          <h2 className="text-lg font-semibold mb-4">잔액 정보</h2>
          <div className="grid grid-cols-2 gap-4">
            <div className="text-center p-4 bg-blue-50 rounded-lg">
              <div className="text-sm text-gray-600 mb-1">사용 가능 포인트</div>
              <div className="text-2xl font-bold text-blue-600">
                {balance.available.toLocaleString()}P
              </div>
            </div>
            <div className="text-center p-4 bg-orange-50 rounded-lg">
              <div className="text-sm text-gray-600 mb-1">7일 내 만료 포인트</div>
              <div className="text-2xl font-bold text-orange-600">
                {balance.expiringSoon.toLocaleString()}P
              </div>
            </div>
          </div>
        </div>
      )}

      <div className="bg-white rounded-lg shadow-md">
        <div className="p-6 border-b border-gray-200">
          <h2 className="text-lg font-semibold">포인트 사용 내역</h2>
        </div>
        
        {history.length === 0 ? (
          <div className="p-6 text-center text-gray-500">
            포인트 내역이 없습니다.
          </div>
        ) : (
          <div className="divide-y divide-gray-200">
            {history.map((item: PointHistoryItemResponse) => (
              <div key={item.id} className="p-6">
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <span className={`font-medium ${getTypeColor(item.type)}`}>
                        {getTypeLabel(item.type)}
                      </span>
                      <span className={`text-lg font-bold ${getTypeColor(item.type)}`}>
                        {item.amount > 0 ? '+' : ''}{item.amount.toLocaleString()}P
                      </span>
                    </div>
                    
                    <div className="text-sm text-gray-600 mb-1">
                      획득일: {formatDate(item.createdAt)} {formatTime(item.createdAt)}
                    </div>
                    
                    {item.expiresAt && (
                      <div className="text-sm">
                        <span className="text-gray-600">만료일: </span>
                        <span className={`${
                          isExpired(item.expiresAt) 
                            ? 'text-red-600 font-medium' 
                            : isExpiringSoon(item.expiresAt)
                            ? 'text-orange-600 font-medium'
                            : 'text-gray-600'
                        }`}>
                          {formatDate(item.expiresAt)} {formatTime(item.expiresAt)}
                          {isExpired(item.expiresAt) && ' (만료됨)'}
                          {isExpiringSoon(item.expiresAt) && !isExpired(item.expiresAt) && ' (곧 만료)'}
                        </span>
                      </div>
                    )}
                  </div>
                  
                  {isExpired(item.expiresAt) && (
                    <span className="px-2 py-1 bg-red-100 text-red-600 rounded-full text-xs font-medium">
                      사용 불가
                    </span>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default PointHistoryPage;