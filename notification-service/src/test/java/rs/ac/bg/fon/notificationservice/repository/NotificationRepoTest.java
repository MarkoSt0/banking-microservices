package rs.ac.bg.fon.notificationservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import rs.ac.bg.fon.notificationservice.TestcontainersConfiguration;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class NotificationRepoTest {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldSaveNotification(){
        notificationRepo.save("testemail@yahoo.com", "Test message");

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM notification WHERE recipient_email = ?",
                Integer.class,
                "testemail@yahoo.com"
        );

        Assertions.assertEquals(1, count);
    }

    @Test
    void shouldSaveMultipleNotifications(){
        notificationRepo.save("test2email@yahoo.com", "Test message2");
        notificationRepo.save("test3email@yahoo.com", "Test message3");
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM notification",
                Integer.class
        );

        Assertions.assertEquals(3, count);
    }

    @Test
    void shouldPersistCorrectMessage(){
        notificationRepo.save("testemail4@yahoo.com", "Test message4");

        String res = jdbcTemplate.queryForObject(
                "SELECT message FROM notification WHERE recipient_email = ?",
                String.class,
                "testemail4@yahoo.com"
        );

        Assertions.assertEquals("Test message4", res);
    }
}
