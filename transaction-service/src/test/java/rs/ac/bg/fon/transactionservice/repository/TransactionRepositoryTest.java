package rs.ac.bg.fon.transactionservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import rs.ac.bg.fon.transactionservice.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldCreateTransaction(){
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        UUID transactionId = transactionRepo.createTransaction(
                senderId,
                receiverId,
                BigDecimal.valueOf(10000)
        );

        assertNotNull(transactionId);
    }

    @Test
    void shouldCreateMultipleTransaction(){
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        UUID transactionId1 = transactionRepo.createTransaction(
                senderId,
                receiverId,
                BigDecimal.valueOf(10000)
        );

        UUID transactionId2 = transactionRepo.createTransaction(
                senderId,
                receiverId,
                BigDecimal.valueOf(12000)
        );

        assertNotNull(transactionId1);
        assertNotNull(transactionId2);
        assertNotEquals(transactionId1, transactionId2);
    }

    @Test
    void shouldPersistCorrectAmount(){
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        UUID transactionId = transactionRepo.createTransaction(
                senderId,
                receiverId,
                BigDecimal.valueOf(10000)
        );

        BigDecimal amount = jdbcTemplate.queryForObject(
                "SELECT amount FROM impl.money_transaction WHERE id = ?",
                BigDecimal.class,
                transactionId
        );

        assertNotNull(transactionId);
        assertNotNull(amount);
        assertEquals(0, amount.compareTo(BigDecimal.valueOf(10000)));
    }

    @Test
    void shouldThrowWhenCreatingNullSenderId(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.createTransaction(
                        null,
                        UUID.randomUUID(),
                        BigDecimal.valueOf(12000)
                )
        );
        assertTrue(ex.getMessage().contains("Sender id cannot be null."));
    }

    @Test
    void shouldThrowWhenCreatingNullReceiverId(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.createTransaction(
                        UUID.randomUUID(),
                        null,
                        BigDecimal.valueOf(12000)
                )
        );
        assertTrue(ex.getMessage().contains("Receiver id cannot be null."));
    }

    @Test
    void shouldThrowWhenCreatingNullAmount(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.createTransaction(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        null
                )
        );
        assertTrue(ex.getMessage().contains("Amount cannot be null."));
    }

    @Test
    void shouldThrowWhenCreatingLowAmount(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.createTransaction(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        BigDecimal.valueOf(-5)
                )
        );
        assertTrue(ex.getMessage().contains("Amount must be greater than zero."));
    }

    @Test
    void shouldThrowWhenCreatingSameId(){
        UUID id = UUID.randomUUID();
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.createTransaction(
                        id,
                        id,
                        BigDecimal.valueOf(12000)
                )
        );
        assertTrue(ex.getMessage().contains("Sender and receiver account cannot be the same."));
    }

}

