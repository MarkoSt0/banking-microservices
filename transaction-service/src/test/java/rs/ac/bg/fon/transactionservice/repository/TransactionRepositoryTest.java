package rs.ac.bg.fon.transactionservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import rs.ac.bg.fon.transactionservice.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String reason = "Test reason message";

    private UUID createTransaction(BigDecimal amount){
        return transactionRepo.createTransaction(
                UUID.randomUUID(),
                UUID.randomUUID(),
                amount
        );
    }

    @Test
    void shouldCreateTransaction(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));

        assertNotNull(transactionId);
    }

    @Test
    void shouldCreateMultipleTransaction(){
        UUID transactionId1 = createTransaction(BigDecimal.valueOf(10000));
        UUID transactionId2 = createTransaction(BigDecimal.valueOf(12000));

        assertNotNull(transactionId1);
        assertNotNull(transactionId2);
        assertNotEquals(transactionId1, transactionId2);
    }

    @Test
    void shouldPersistCorrectAmount(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));

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
    void shouldCreatePendingTransaction(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));

        String status = jdbcTemplate.queryForObject(
                "SELECT transaction_status FROM impl.money_transaction WHERE id = ?",
                String.class,
                transactionId
        );

        assertEquals("PENDING", status);
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

    @Test
    void shouldCompleteTransaction(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));
        transactionRepo.completeTransaction(transactionId);

        String status = jdbcTemplate.queryForObject(
                "SELECT transaction_status FROM impl.money_transaction WHERE id = ?",
                String.class,
                transactionId
        );
        assertNotNull(status);
        assertEquals("COMPLETED", status);
    }

    @Test
    void shouldThrowWhenCompletingNullId(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.completeTransaction(
                        null
                )
        );
        assertTrue(ex.getMessage().contains("Transaction id cannot be null."));
    }

    @Test
    void shouldThrowWhenCompletingInvalidTransaction(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.completeTransaction(
                        UUID.randomUUID()
                )
        );
        assertTrue(ex.getMessage().contains("Transaction does not exist."));
    }

    @Test
    void shouldThrowWhenCompletingNotPendingTransaction(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));
        transactionRepo.completeTransaction(transactionId);

        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.completeTransaction(
                        transactionId
                )
        );
        assertTrue(ex.getMessage().contains("Only pending transactions can be completed."));
    }

    @Test
    void shouldFailTransaction(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));
        transactionRepo.failTransaction(transactionId, reason);

        Map<String, Object> result = jdbcTemplate.queryForMap(
                "SELECT transaction_status, failure_reason FROM impl.money_transaction WHERE id = ?",
                transactionId
        );
        assertNotNull(result);
        assertEquals("FAILED", result.get("transaction_status"));
        assertEquals(reason, result.get("failure_reason"));
    }

    @Test
    void shouldThrowWhenFailingNullId(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.failTransaction(
                        null,
                        reason
                )
        );
        assertTrue(ex.getMessage().contains("Transaction id cannot be null."));
    }

    @Test
    void shouldThrowWhenFailingNullMessage(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.failTransaction(
                        transactionId,
                        null
                )
        );
        assertTrue(ex.getMessage().contains("Failure reason cannot be null."));
    }

    @Test
    void shouldThrowWhenFailingEmptyMessage(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.failTransaction(
                        transactionId,
                        ""
                )
        );
        assertTrue(ex.getMessage().contains("Failure reason cannot be empty."));
    }

    @Test
    void shouldThrowWhenFailingInvalidTransaction(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.failTransaction(
                        UUID.randomUUID(),
                        reason
                )
        );
        assertTrue(ex.getMessage().contains("Transaction does not exist."));
    }

    @Test
    void shouldThrowWhenFailingCompletedTransaction(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));
        transactionRepo.completeTransaction(transactionId);

        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.failTransaction(
                        transactionId,
                        reason
                )
        );
        assertTrue(ex.getMessage().contains("Only pending transactions can be failed."));
    }

    @Test
    void shouldThrowWhenFailingFailedTransaction(){
        UUID transactionId = createTransaction(BigDecimal.valueOf(10000));
        transactionRepo.failTransaction(transactionId, reason);

        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> transactionRepo.failTransaction(
                        transactionId,
                        reason
                )
        );
        assertTrue(ex.getMessage().contains("Only pending transactions can be failed."));
    }
}

