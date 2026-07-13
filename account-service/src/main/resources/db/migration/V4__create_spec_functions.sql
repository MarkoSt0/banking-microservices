CREATE OR REPLACE FUNCTION spec.fnc_getBalance(
       account_id UUID
)
RETURNS NUMERIC
LANGUAGE plpgsql
AS
$$
DECLARE v_balance NUMERIC;
BEGIN
    SELECT balance
    INTO v_balance
    FROM impl.account
    WHERE id = account_id
    AND deleted = FALSE;

    RETURN v_balance;
END;
$$;