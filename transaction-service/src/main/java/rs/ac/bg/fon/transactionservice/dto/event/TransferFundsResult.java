package rs.ac.bg.fon.transactionservice.dto.event;

import rs.ac.bg.fon.transactionservice.enums.TransferStatus;

import java.util.UUID;

public record TransferFundsResult(
        UUID transactionId,
        TransferStatus status,
        String errorCode
) { }
