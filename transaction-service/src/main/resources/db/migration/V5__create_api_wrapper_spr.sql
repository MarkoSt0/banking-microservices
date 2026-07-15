CREATE OR REPLACE PROCEDURE api.spr_create_transaction(
    IN sender_id UUID,
    IN receiver_id UUID,
    IN amount NUMERIC(19,2),
    OUT transaction_id UUID
)
LANGUAGE plpgsql
AS
$$
BEGIN
    IF sender_id IS NULL THEN
       RAISE EXCEPTION 'Sender id cannot be null.';
    END IF;

    IF receiver_id IS NULL THEN
       RAISE EXCEPTION 'Receiver id cannot be null.';
    END IF;

    IF amount IS NULL THEN
       RAISE EXCEPTION 'Amount cannot be null.';
    END IF;

    IF amount <= 0 THEN
        RAISE EXCEPTION 'Amount must be greater than zero.';
    END IF;

    IF sender_id = receiver_id THEN
        RAISE EXCEPTION 'Sender and receiver account cannot be the same.';
    END IF;

    CALL spec.spr_create_transaction(
        sender_id,
        receiver_id,
        amount,
        transaction_id
    );
END;
$$;

CREATE OR REPLACE PROCEDURE api.spr_complete_transaction(
    IN transaction_id UUID
)
LANGUAGE plpgsql
AS
$$
BEGIN
    IF transaction_id IS NULL THEN
       RAISE EXCEPTION 'Transaction id cannot be null.';
    END IF;

    CALL spec.spr_complete_transaction(
        transaction_id
    );
END;
$$;

CREATE OR REPLACE PROCEDURE api.spr_fail_transaction(
    IN transaction_id UUID,
    IN p_failure_reason VARCHAR(255)
)
LANGUAGE plpgsql
AS
$$
BEGIN
    IF transaction_id IS NULL THEN
       RAISE EXCEPTION 'Transaction id cannot be null.';
    END IF;

    IF p_failure_reason IS NULL THEN
        RAISE EXCEPTION 'Failure reason cannot be null.';
    END IF;

    IF LENGTH(TRIM(p_failure_reason)) = 0 THEN
        RAISE EXCEPTION 'Failure reason cannot be empty.';
    END IF;

    CALL spec.spr_fail_transaction(
        transaction_id,
        p_failure_reason
    );
END;
$$;