-- 일일 예산 테이블을 룰렛 예산 테이블로 이름 변경
ALTER TABLE daily_budget RENAME TO roulette_budget;

-- 테이블 주석 업데이트
COMMENT ON TABLE roulette_budget IS '룰렛 예산 관리';
COMMENT ON COLUMN roulette_budget.id IS '룰렛 예산 ID';
COMMENT ON COLUMN roulette_budget.budget_date IS '예산 기준 날짜 (yyyy-MM-dd)';
COMMENT ON COLUMN roulette_budget.total_budget IS '해당 일자의 총 예산';
COMMENT ON COLUMN roulette_budget.remaining_budget IS '현재 남은 예산';
COMMENT ON COLUMN roulette_budget.created_at IS '룰렛 예산 생성 일시';
COMMENT ON COLUMN roulette_budget.updated_at IS '룰렛 예산 수정 일시';