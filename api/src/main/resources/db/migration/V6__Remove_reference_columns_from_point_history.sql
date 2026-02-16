-- reference_type, reference_id 컬럼 삭제
ALTER TABLE point_history DROP COLUMN reference_type;
ALTER TABLE point_history DROP COLUMN reference_id;