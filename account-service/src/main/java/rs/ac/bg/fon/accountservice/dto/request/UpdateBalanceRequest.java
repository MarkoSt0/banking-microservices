package rs.ac.bg.fon.accountservice.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateBalanceRequest(
        @NotNull UUID uuid,
        @NotNull BigDecimal amount) {
}
