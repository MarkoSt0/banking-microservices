package rs.ac.bg.fon.notificationservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepo {
    private final JdbcTemplate template;
    public void save(String recipientEmail, String message){
        String sql = "INSERT INTO notification (recipient_email, message) VALUES (?,?)";
        template.update(
                sql,
                recipientEmail,
                message
        );
    }
}
