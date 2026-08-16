CREATE OR REPLACE FUNCTION api.fnc_getBalance(
       account_id UUID
)
RETURNS NUMERIC
LANGUAGE plpgsql
SECURITY DEFINER
AS
$$
BEGIN
    IF account_id IS NULL THEN
        RAISE EXCEPTION USING ERRCODE = 'P1003', MESSAGE = 'Account id cannot be null.';
    END IF;

    RETURN spec.fnc_getBalance(account_id);
END;
$$;