-- 포인트 이력 테이블에서 만료일시 컬럼 제거
-- created_at 기준으로 계산하는 방식으로 변경
ALTER TABLE point_history DROP COLUMN IF EXISTS expired_at;