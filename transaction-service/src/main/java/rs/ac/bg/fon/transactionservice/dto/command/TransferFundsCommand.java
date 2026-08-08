package rs.ac.bg.fon.transactionservice.dto.command;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferFundsCommand(
    UUID senderAccountId,
    UUID receiverAccountId,
    BigDecimal amount
) { }
