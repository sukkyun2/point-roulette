interface PlaceholderPageProps {
  title: string;
  description?: string;
}

export default function PlaceholderPage({ title, description }: PlaceholderPageProps) {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-4">{title}</h1>
      {description && (
        <p className="text-gray-600 mb-8">{description}</p>
      )}
      
      <div className="bg-white rounded-lg shadow-md p-12">
        <div className="text-center">
          <div className="mb-4">
            <span className="text-6xl">🚧</span>
          </div>
          <h2 className="text-xl font-semibold text-gray-700 mb-2">준비 중입니다</h2>
          <p className="text-gray-500">
            이 기능은 현재 개발 중입니다. 곧 만나보실 수 있어요!
          </p>
        </div>
      </div>
    </div>
  );
}