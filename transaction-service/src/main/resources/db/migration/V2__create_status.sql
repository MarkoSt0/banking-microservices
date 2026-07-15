CREATE TYPE impl.transaction_status AS ENUM(
       'PENDING',
       'COMPLETED',
       'FAILED',
       'CANCELLED'
);