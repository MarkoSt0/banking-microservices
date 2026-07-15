CREATE OR REPLACE PROCEDURE spec.spr_create_transaction(
    IN sender_id UUID,
    IN receiver_id UUID,
    IN amount NUMERIC(19,2),
    OUT transaction_id UUID
)
LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO impl.money_transaction(
        sender_account_id,
        receiver_account_id,
        amount
    )
    VALUES(
        sender_id,
        receiver_id,
        amount
    )
    RETURNING id
    INTO transaction_id;
END;
$$;

CREATE OR REPLACE PROCEDURE spec.spr_complete_transaction(
    IN transaction_id UUID
)
LANGUAGE plpgsql
AS
$$
DECLARE
    v_status impl.transaction_status;
BEGIN
    SELECT transaction_status
    INTO v_status
    FROM impl.money_transaction
    WHERE id = transaction_id
    FOR UPDATE;

--     Verify transaction exists
    IF NOT FOUND THEN
       RAISE EXCEPTION 'Transaction does not exist.';
    END IF;

--     Verify transaction status
    IF v_status <> 'PENDING' THEN
        RAISE EXCEPTION 'Only pending transactions can be completed.';
    END IF;

    UPDATE impl.money_transaction
    SET transaction_status = 'COMPLETED'
    WHERE id = transaction_id;
END;
$$;

CREATE OR REPLACE PROCEDURE spec.spr_fail_transaction(
    IN transaction_id UUID,
    IN p_failure_reason VARCHAR(255)
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    v_status impl.transaction_status;
BEGIN
    SELECT transaction_status
    INTO v_status
    FROM impl.money_transaction
    WHERE id = transaction_id
    FOR UPDATE;

--     Verify transaction exists
    IF NOT FOUND THEN RAISE EXCEPTION 'Transaction does not exist.';
    END IF;

--     Verify transaction status
    IF v_status <> 'PENDING' THEN
        RAISE EXCEPTION 'Only pending transactions can be failed.';
    END IF;

    UPDATE impl.money_transaction
    SET transaction_status = 'FAILED',
        failure_reason = p_failure_reason
    WHERE id = transaction_id;
END;
$$;