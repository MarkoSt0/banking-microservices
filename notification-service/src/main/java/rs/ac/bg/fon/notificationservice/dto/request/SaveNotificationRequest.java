package rs.ac.bg.fon.notificationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SaveNotificationRequest(
    @NotNull @NotBlank @Length(max = 255) String recipientEmail,
    @NotNull @NotBlank String message
) { }
