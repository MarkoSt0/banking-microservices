CREATE OR REPLACE FUNCTION api.fnc_getBalance(
       account_id UUID
)
RETURNS NUMERIC
LANGUAGE plpgsql
AS
$$
BEGIN
    IF account_id IS NULL THEN
        RAISE EXCEPTION 'Account id cannot be null.';
    END IF;

    RETURN spec.fnc_getBalance(account_id);
END;
$$;