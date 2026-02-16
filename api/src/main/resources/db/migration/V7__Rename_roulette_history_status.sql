ALTER TABLE roulette_history DROP CONSTRAINT IF EXISTS roulette_history_status_check;
ALTER TABLE roulette_history
    ADD CONSTRAINT roulette_history_status_check
CHECK (status IN ('SUCCESS', 'CANCELED'));