import React, { useState } from 'react';
import { useLoginMutation } from '../hooks/useLoginMutation';
import { useAuth } from '../contexts/AuthContext';

export const LoginForm: React.FC = () => {
  const [nickname, setNickname] = useState('');
  const { setUser, refreshUser } = useAuth();

  const loginMutation = useLoginMutation({
    onSuccess: userData => {
      if (!userData) return alert('등록되지않은 닉네임입니다');

      localStorage.setItem('token', userData.token);
      setUser({
        id: userData.userId,
        nickname: userData.nickname,
        token: userData.token,
      });
      refreshUser();
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!nickname.trim()) {
      alert('닉네임을 입력해주세요.');
      return;
    }

    loginMutation.mutate({
      data: { nickname: nickname.trim() },
    });
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="max-w-md w-full space-y-8 p-8 bg-white rounded-lg shadow-md">
        <div className="text-center">
          <h2 className="text-3xl font-bold text-gray-900">포인트 룰렛</h2>
          <p className="mt-2 text-gray-600">
            닉네임을 입력하여 게임에 참여하세요
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label
              htmlFor="nickname"
              className="block text-sm font-medium text-gray-700"
            >
              닉네임
            </label>
            <input
              type="text"
              id="nickname"
              value={nickname}
              onChange={e => setNickname(e.target.value)}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              placeholder="닉네임을 입력하세요"
              disabled={loginMutation.isPending}
            />
          </div>

          <button
            type="submit"
            disabled={loginMutation.isPending}
            className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loginMutation.isPending ? '로그인 중...' : '로그인'}
          </button>
        </form>
      </div>
    </div>
  );
};
