package rs.ac.bg.fon.accountservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotNull @NotBlank @Length(max = 255) String ownerName,
        @NotNull @Min(1) BigDecimal initialBalance,
        @NotNull String currency) {
}
