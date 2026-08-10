package rs.ac.bg.fon.transactionservice.dto.response;

import java.util.UUID;

public record CreateTransactionResponse(
        UUID transactionId
) { }
