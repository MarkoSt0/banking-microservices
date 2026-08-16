package rs.ac.bg.fon.accountservice.dto.command;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferFundsCommand(
        UUID transactionId,
        UUID senderAccountId,
        UUID receiverAccountId,
        BigDecimal amount
) { }
