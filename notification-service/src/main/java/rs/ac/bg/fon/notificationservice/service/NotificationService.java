package rs.ac.bg.fon.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import rs.ac.bg.fon.notificationservice.dto.request.SaveNotificationRequest;
import rs.ac.bg.fon.notificationservice.repository.NotificationRepo;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepo notificationRepo;

    public void saveNotification(@Validated SaveNotificationRequest request){
        notificationRepo.save(request.recipientEmail(), request.message());
    }

}
