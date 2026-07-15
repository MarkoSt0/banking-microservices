CREATE TABLE impl.money_transaction(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_account_id UUID NOT NULL,
    receiver_account_id UUID NOT NULL,
    amount NUMERIC(19,2) NOT NULL CHECK ( amount > 0 ),
    transaction_status impl.transaction_status NOT NULL DEFAULT 'PENDING',
    failure_reason VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);