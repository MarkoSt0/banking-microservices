CREATE OR REPLACE PROCEDURE spec.spr_updateBalance(
       IN account_id UUID,
       IN amount NUMERIC
)

LANGUAGE plpgsql
AS
$$
DECLARE
       current_balance NUMERIC;
BEGIN
--     Lock account row and retrieve curren balance
    SELECT balance
    INTO current_balance
    FROM impl.account
    WHERE id = account_id
    FOR UPDATE;

--     Verify account exists
    IF NOT FOUND THEN
           RAISE EXCEPTION 'Account does not exists.';
    END IF;

--     Verify sufficient funds
    IF current_balance + amount < 0 THEN
        RAISE EXCEPTION 'Insufficient funds.';
    END IF;

--     Update account balance
    UPDATE impl.account
    SET balance = balance + amount
    WHERE id = account_id;

END;
$$;

CREATE OR REPLACE PROCEDURE spec.spr_createAccount(
    IN owner_name VARCHAR(100),
    IN initial_balance NUMERIC,
    IN currency impl.currency_type,
    OUT account_id UUID
)
LANGUAGE plpgsql
AS
$$
BEGIN

    INSERT INTO impl.account(
        owner_name,
        balance,
        currency
    )
    VALUES(
        owner_name,
        initial_balance,
        currency
    )
    RETURNING id
    INTO account_id;

END;
$$;