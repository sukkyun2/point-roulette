-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    nickname VARCHAR(255) UNIQUE NOT NULL,
    balance BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS '사용자 정보';
COMMENT ON COLUMN users.id IS '사용자 ID';
COMMENT ON COLUMN users.nickname IS '닉네임';
COMMENT ON COLUMN users.balance IS '보유 포인트';
COMMENT ON COLUMN users.created_at IS '사용자 생성 일시';
COMMENT ON COLUMN users.updated_at IS '사용자 정보 수정 일시';

-- 일일 예산 테이블
CREATE TABLE IF NOT EXISTS daily_budget (
    id BIGSERIAL PRIMARY KEY,
    budget_date DATE UNIQUE NOT NULL,
    total_budget BIGINT NOT NULL,
    remaining_budget BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE daily_budget IS '일일 예산 관리';
COMMENT ON COLUMN daily_budget.id IS '일일 예산 ID';
COMMENT ON COLUMN daily_budget.budget_date IS '예산 기준 날짜 (yyyy-MM-dd)';
COMMENT ON COLUMN daily_budget.total_budget IS '해당 일자의 총 예산';
COMMENT ON COLUMN daily_budget.remaining_budget IS '현재 남은 예산';
COMMENT ON COLUMN daily_budget.created_at IS '일일 예산 생성 일시';
COMMENT ON COLUMN daily_budget.updated_at IS '일일 예산 수정 일시';

-- 상품 테이블
CREATE TABLE IF NOT EXISTS product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE product IS '상품 정보';
COMMENT ON COLUMN product.id IS '상품 ID';
COMMENT ON COLUMN product.name IS '상품명';
COMMENT ON COLUMN product.price IS '상품 가격';
COMMENT ON COLUMN product.created_at IS '상품 등록 일시';
COMMENT ON COLUMN product.updated_at IS '상품 수정 일시';

-- 주문 테이블
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    product_id BIGINT NOT NULL REFERENCES product(id),
    price_at_order BIGINT NOT NULL,
    status VARCHAR(20) CHECK (status IN ('COMPLETED', 'CANCELED')) DEFAULT 'COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    canceled_at TIMESTAMP
);

COMMENT ON TABLE orders IS '주문 정보';
COMMENT ON COLUMN orders.id IS '주문 ID';
COMMENT ON COLUMN orders.user_id IS '주문한 사용자 ID (참조: users.id)';
COMMENT ON COLUMN orders.product_id IS '주문한 상품 ID (참조: product.id)';
COMMENT ON COLUMN orders.price_at_order IS '주문 당시 상품 가격';
COMMENT ON COLUMN orders.status IS '주문 상태 (COMPLETED: 주문 완료, CANCELED: 주문 취소)';
COMMENT ON COLUMN orders.created_at IS '주문 생성 일시';
COMMENT ON COLUMN orders.canceled_at IS '주문 취소 일시 (취소되지 않은 경우 NULL)';

-- 룰렛 참여 기록 테이블
CREATE TABLE IF NOT EXISTS roulette_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    event_date DATE NOT NULL,
    earn_point BIGINT NOT NULL,
    status VARCHAR(20) CHECK (status IN ('SUCCESS', 'CANCELED')) DEFAULT 'SUCCESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE roulette_history IS '룰렛 참여 기록';
COMMENT ON COLUMN roulette_history.id IS '룰렛 참여 ID';
COMMENT ON COLUMN roulette_history.user_id IS '참여 사용자 ID (참조: users.id)';
COMMENT ON COLUMN roulette_history.event_date IS '룰렛 참여 기준 날짜 (yyyy-MM-dd)';
COMMENT ON COLUMN roulette_history.earn_point IS '해당 참여로 지급된 포인트';
COMMENT ON COLUMN roulette_history.status IS '룰렛 상태 (SUCCESS: 지급 완료, CANCELED: 관리자에 의한 참여 취소)';
COMMENT ON COLUMN roulette_history.created_at IS '룰렛 참여 기록 생성 일시';

-- 포인트 이력 테이블
CREATE TABLE IF NOT EXISTS point_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    amount BIGINT NOT NULL,
    type VARCHAR(20) CHECK (type IN ('EARN', 'USE', 'REFUND', 'RECLAIM', 'EXPIRE')) NOT NULL,
    reference_type VARCHAR(20) CHECK (reference_type IN ('ROULETTE', 'ORDER', 'NONE')) DEFAULT 'NONE',
    reference_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE point_history IS '포인트 변동 이력';
COMMENT ON COLUMN point_history.id IS '포인트 이력 ID';
COMMENT ON COLUMN point_history.user_id IS '포인트 소유 사용자 ID (참조: users.id)';
COMMENT ON COLUMN point_history.amount IS '포인트 변동 금액 (+적립, -차감)';
COMMENT ON COLUMN point_history.type IS '포인트 유형 (EARN: 적립, USE: 사용, REFUND: 주문 환불, RECLAIM: 룰렛 취소 회수, EXPIRE: 만료 차감)';
COMMENT ON COLUMN point_history.reference_type IS '참조 이벤트 유형 (ROULETTE: 룰렛, ORDER: 주문, NONE: 직접 참조 없음)';
COMMENT ON COLUMN point_history.reference_id IS '참조 이벤트 ID (reference_type에 따라 roulette_history.id 또는 orders.id)';
COMMENT ON COLUMN point_history.created_at IS '포인트 이력 생성 일시';