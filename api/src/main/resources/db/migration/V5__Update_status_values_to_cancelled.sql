-- Update status values from CANCELED to CANCELLED for consistency with backend domain models

-- Update existing data in orders table
UPDATE orders 
SET status = 'CANCELLED' 
WHERE status = 'CANCELED';

-- Update existing data in roulette_history table
UPDATE roulette_history 
SET status = 'CANCELLED' 
WHERE status = 'CANCELED';

-- Drop existing CHECK constraints
ALTER TABLE orders DROP CONSTRAINT IF EXISTS orders_status_check;
ALTER TABLE roulette_history DROP CONSTRAINT IF EXISTS roulette_history_status_check;

-- Add new CHECK constraints with CANCELLED spelling
ALTER TABLE orders 
ADD CONSTRAINT orders_status_check 
CHECK (status IN ('COMPLETED', 'CANCELLED'));

ALTER TABLE roulette_history 
ADD CONSTRAINT roulette_history_status_check 
CHECK (status IN ('SUCCESS', 'CANCELLED'));

-- Update comments to reflect the new status values
COMMENT ON COLUMN orders.status IS '주문 상태 (COMPLETED: 주문 완료, CANCELLED: 주문 취소)';
COMMENT ON COLUMN roulette_history.status IS '룰렛 상태 (SUCCESS: 지급 완료, CANCELLED: 관리자에 의한 참여 취소)';