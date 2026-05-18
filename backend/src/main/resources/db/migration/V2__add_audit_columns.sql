-- BaseEntity audit 컬럼 추가 (created_by, updated_by, deleted_at, deleted_by)
-- 기존 스키마에 누락된 updated_at, created_at 컬럼도 함께 추가

-- assistant_user
ALTER TABLE assistant_user ADD COLUMN created_by VARCHAR(100);
ALTER TABLE assistant_user ADD COLUMN updated_by VARCHAR(100);
ALTER TABLE assistant_user ADD COLUMN deleted_at TIMESTAMP;
ALTER TABLE assistant_user ADD COLUMN deleted_by VARCHAR(100);

-- conversation
ALTER TABLE conversation ADD COLUMN created_by VARCHAR(100);
ALTER TABLE conversation ADD COLUMN updated_by VARCHAR(100);
ALTER TABLE conversation ADD COLUMN deleted_at TIMESTAMP;
ALTER TABLE conversation ADD COLUMN deleted_by VARCHAR(100);

-- chat_message
ALTER TABLE chat_message ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();
ALTER TABLE chat_message ADD COLUMN created_by VARCHAR(100);
ALTER TABLE chat_message ADD COLUMN updated_by VARCHAR(100);
ALTER TABLE chat_message ADD COLUMN deleted_at TIMESTAMP;
ALTER TABLE chat_message ADD COLUMN deleted_by VARCHAR(100);

-- message_source
ALTER TABLE message_source ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT NOW();
ALTER TABLE message_source ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();
ALTER TABLE message_source ADD COLUMN created_by VARCHAR(100);
ALTER TABLE message_source ADD COLUMN updated_by VARCHAR(100);
ALTER TABLE message_source ADD COLUMN deleted_at TIMESTAMP;
ALTER TABLE message_source ADD COLUMN deleted_by VARCHAR(100);

-- token_usage_log
ALTER TABLE token_usage_log ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();
ALTER TABLE token_usage_log ADD COLUMN created_by VARCHAR(100);
ALTER TABLE token_usage_log ADD COLUMN updated_by VARCHAR(100);
ALTER TABLE token_usage_log ADD COLUMN deleted_at TIMESTAMP;
ALTER TABLE token_usage_log ADD COLUMN deleted_by VARCHAR(100);
