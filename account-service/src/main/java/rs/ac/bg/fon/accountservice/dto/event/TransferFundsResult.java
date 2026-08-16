package rs.ac.bg.fon.accountservice.dto.event;

import rs.ac.bg.fon.accountservice.enums.TransferStatus;

import java.util.UUID;

public record TransferFundsResult(
        UUID transactionId,
        TransferStatus status,
        String errorCode
) { }
