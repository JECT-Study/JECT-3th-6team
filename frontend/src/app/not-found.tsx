import Link from 'next/link';

export default function NotFound() {
  return (
    <div className="min-h-screen bg-gray-900 flex items-center justify-center px-6">
      <div className="text-center">
        <h1 className="text-6xl font-extrabold text-white animate-bounce mb-4">
          404
        </h1>
        <p className="text-2xl text-gray-300 mb-2">페이지를 찾을 수 없습니다</p>
        <p className="text-gray-400 mb-6">
          요청하신 페이지가 존재하지 않거나, 이동되었을 수 있습니다.
        </p>
        <Link
          href="/"
          className="inline-block px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-700 transition"
        >
          홈으로 돌아가기
        </Link>
      </div>
    </div>
  );
}
