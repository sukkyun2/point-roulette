import { useDashboard } from '../hooks/useDashboard';

interface DashboardProps {}

export default function Dashboard({}: DashboardProps) {
  const { dashboardData, isLoading, error } = useDashboard();

  const formatNumber = (num: number | undefined) => {
    if (num === undefined) return '0';
    return num.toLocaleString();
  };

  if (isLoading) {
    return (
      <div className="p-8 flex justify-center items-center min-h-96">
        <div className="text-lg text-gray-500">데이터를 불러오는 중...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-8 flex justify-center items-center min-h-96">
        <div className="text-lg text-red-500">데이터를 불러오는데 실패했습니다.</div>
      </div>
    );
  }

  const totalBudget = dashboardData?.totalBudget ?? 0;
  const remainingBudget = dashboardData?.remainingBudget ?? 0;
  const participantCount = dashboardData?.participantCount ?? 0;
  const awardedPoints = totalBudget - remainingBudget;

  return (
    <div className="p-8 min-h-screen">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">대시보드</h1>
      
      <div className="grid grid-cols-2 gap-8 h-[calc(100vh-200px)]">
        {/* 오늘의 총 예산 */}
        <div className="bg-white p-12 rounded-lg shadow-md flex items-center justify-center">
          <div className="text-center">
            <div className="p-6 rounded-full bg-blue-100 mx-auto mb-6 w-fit">
              <span className="text-6xl">💰</span>
            </div>
            <p className="text-lg font-medium text-gray-600 mb-4">오늘의 총 예산</p>
            <p className="text-5xl font-bold text-gray-900">{formatNumber(totalBudget)} P</p>
          </div>
        </div>

        {/* 오늘의 잔여 예산 */}
        <div className="bg-white p-12 rounded-lg shadow-md flex items-center justify-center">
          <div className="text-center">
            <div className="p-6 rounded-full bg-green-100 mx-auto mb-6 w-fit">
              <span className="text-6xl">💵</span>
            </div>
            <p className="text-lg font-medium text-gray-600 mb-4">오늘의 잔여 예산</p>
            <p className="text-5xl font-bold text-green-600">{formatNumber(remainingBudget)} P</p>
          </div>
        </div>

        {/* 오늘의 고유 참여자 수 */}
        <div className="bg-white p-12 rounded-lg shadow-md flex items-center justify-center">
          <div className="text-center">
            <div className="p-6 rounded-full bg-purple-100 mx-auto mb-6 w-fit">
              <span className="text-6xl">👥</span>
            </div>
            <p className="text-lg font-medium text-gray-600 mb-4">오늘의 고유 참여자 수</p>
            <p className="text-5xl font-bold text-purple-600">{formatNumber(participantCount)}명</p>
          </div>
        </div>

        {/* 오늘 지급된 총 포인트 */}
        <div className="bg-white p-12 rounded-lg shadow-md flex items-center justify-center">
          <div className="text-center">
            <div className="p-6 rounded-full bg-orange-100 mx-auto mb-6 w-fit">
              <span className="text-6xl">🎁</span>
            </div>
            <p className="text-lg font-medium text-gray-600 mb-4">오늘 지급된 총 포인트</p>
            <p className="text-5xl font-bold text-orange-600">{formatNumber(awardedPoints)} P</p>
          </div>
        </div>
      </div>
    </div>
  );
}