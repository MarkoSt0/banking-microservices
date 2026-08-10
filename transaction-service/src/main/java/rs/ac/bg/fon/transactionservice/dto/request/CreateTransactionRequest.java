package rs.ac.bg.fon.transactionservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequest(
        UUID senderAccountId,
        UUID receiverAccountId,
        BigDecimal amount
) { }
