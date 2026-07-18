CREATE OR REPLACE PROCEDURE api.update_balance(
       IN account_id UUID,
       IN amount NUMERIC
)
LANGUAGE plpgsql
AS
$$
BEGIN
    IF account_id IS NULL THEN
       RAISE EXCEPTION 'Account id cannot be null.';
    END IF;

    IF amount IS NULL THEN
       RAISE EXCEPTION 'Amount cannot be null.';
    END IF;

    IF amount = 0 THEN
        RAISE EXCEPTION 'Amount cannot be zero.';
    END IF;

    CALL spec.spr_updateBalance(
        account_id,
        amount
    );
END;
$$;

CREATE OR REPLACE PROCEDURE api.create_account(
    IN owner_name VARCHAR(100),
    IN initial_balance NUMERIC,
    IN currency VARCHAR(3),
    OUT account_id UUID
)
LANGUAGE plpgsql
AS
$$
BEGIN

    IF owner_name IS NULL OR TRIM(owner_name) = '' THEN
        RAISE EXCEPTION 'Owner name cannot be empty.';
    END IF;

    IF initial_balance IS NULL THEN
        RAISE EXCEPTION 'Initial balance cannot be null.';
    END IF;

    IF initial_balance < 0 THEN
        RAISE EXCEPTION 'Initial balance cannot be negative.';
    END IF;

    IF currency IS NULL THEN
        RAISE EXCEPTION 'Currency cannot be null.';
    END IF;

    CALL spec.spr_createAccount(
        owner_name,
        initial_balance,
        currency::impl.currency_type,
        account_id
    );

END;
$$;