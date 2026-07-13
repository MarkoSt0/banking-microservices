CREATE TABLE impl.account(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_name VARCHAR(100) NOT NULL,
    balance NUMERIC(19,2) NOT NULL CHECK ( balance >= 0 ),
    currency impl.currency_type NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);