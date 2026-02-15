-- 포인트 이력 테이블에 만료일시 컬럼 추가
ALTER TABLE point_history ADD COLUMN IF NOT EXISTS expired_at TIMESTAMP;

COMMENT ON COLUMN point_history.expired_at IS '포인트 만료 일시 (적립된 포인트의 경우만 해당)';