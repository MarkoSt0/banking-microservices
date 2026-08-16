package rs.ac.bg.fon.accountservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import rs.ac.bg.fon.accountservice.TestcontainersConfiguration;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class AccountRepoTest {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID createTestAccount() {
        return accountRepo.createAccount(
                "Test Person",
                BigDecimal.valueOf(12000),
                "RSD"
        );
    }

    @Test
    void shouldCreateAccount(){
        UUID id = accountRepo.createAccount(
                "Test Person",
                BigDecimal.valueOf(12000),
                "RSD"
        );

        assertNotNull(id);
    }

    @Test
    void shouldCreateMultipleAccounts(){
        UUID id1 = accountRepo.createAccount(
                "Test Person 1",
                BigDecimal.valueOf(16000),
                "RSD"
        );

        UUID id2 = accountRepo.createAccount(
                "Test Person 2",
                BigDecimal.valueOf(22000),
                "EUR"
        );

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
    }

    @Test
    void shouldThrowWhenCreatingEmptyOwnerName(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        "",
                        BigDecimal.valueOf(12000),
                        "RSD"
                )
        );
        assertInstanceOf(SQLException.class, ex.getMostSpecificCause());

        SQLException sqlException = (SQLException) ex.getMostSpecificCause();

        assertEquals("P1005", sqlException.getSQLState());
    }

    @Test
    void shouldThrowWhenCreatingNullOwnerName(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        null,
                        BigDecimal.valueOf(12000),
                        "RSD"
                )
        );
        assertInstanceOf(SQLException.class, ex.getMostSpecificCause());

        SQLException sqlException = (SQLException) ex.getMostSpecificCause();

        assertEquals("P1005", sqlException.getSQLState());
    }

    @Test
    void shouldThrowWhenCreatingNullBalance(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        "Test Person",
                        null,
                        "RSD"
                )
        );
        assertInstanceOf(SQLException.class, ex.getMostSpecificCause());

        SQLException sqlException = (SQLException) ex.getMostSpecificCause();

        assertEquals("P1002", sqlException.getSQLState());
        assertTrue(ex.getMessage().contains("Initial balance cannot be null."));
    }

    @Test
    void shouldThrowWhenCreatingNegativeBalance(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        "Test Person",
                        BigDecimal.valueOf(-1000),
                        "USD"
                )
        );
        assertInstanceOf(SQLException.class, ex.getMostSpecificCause());

        SQLException sqlException = (SQLException) ex.getMostSpecificCause();

        assertEquals("P1002", sqlException.getSQLState());
        assertTrue(ex.getMessage().contains("Initial balance cannot be negative."));
    }

    @Test
    void shouldThrowWhenCreatingNullCurrency(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        "Test Person",
                        BigDecimal.valueOf(12000),
                        null
                )
        );

        assertInstanceOf(SQLException.class, ex.getMostSpecificCause());

        SQLException sqlException = (SQLException) ex.getMostSpecificCause();

        assertEquals("P1006", sqlException.getSQLState());
        assertTrue(ex.getMessage().contains("Currency cannot be null."));
    }

    @Test
    void shouldUpdateBalanceDebit(){
        UUID id = createTestAccount();

        accountRepo.updateBalance(
                id,
                BigDecimal.valueOf(-10000)
        );

        BigDecimal result = jdbcTemplate.queryForObject(
                "SELECT balance FROM impl.account WHERE id = ?",
                BigDecimal.class,
                id
        );

        assertNotNull(result);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(2000)));
        assertNotNull(id);
    }

    @Test
    void shouldUpdateBalanceCredit(){
        UUID id = createTestAccount();

        accountRepo.updateBalance(
                id,
                BigDecimal.valueOf(10000)
        );

        BigDecimal result = jdbcTemplate.queryForObject(
                "SELECT balance FROM impl.account WHERE id = ?",
                BigDecimal.class,
                id
        );

        assertNotNull(result);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(22000)));
        assertNotNull(id);
    }

    @Test
    void shouldThrowWhenUpdatingBalanceNullAccountId(){
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> accountRepo.updateBalance(
                        null,
                        BigDecimal.valueOf(100)
                )
        );

        assertInstanceOf(SQLException.class, ex.getMostSpecificCause());

        SQLException sqlException = (SQLException) ex.getMostSpecificCause();

        assertEquals("P1003", sqlException.getSQLState());
        assertTrue(ex.getMessage().contains("Account id cannot be null."));
    }

    @Test
    void shouldThrowWhenUpdatingBalanceNullAmount(){
        UUID id = createTestAccount();

        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> accountRepo.updateBalance(
                        id,
                        null
                )
        );

        assertInstanceOf(SQLException.class, ex.getMostSpecificCause());

        SQLException sqlException = (SQLException) ex.getMostSpecificCause();

        assertEquals("P1004", sqlException.getSQLState());
        assertTrue(ex.getMessage().contains("Amount cannot be null."));
    }

    @Test
    void shouldThrowWhenUpdatingBalanceZeroAmount(){
        UUID id = accountRepo.createAccount(
                "Test Person",
                BigDecimal.valueOf(12000),
                "RSD"
        );

        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> accountRepo.updateBalance(
                        id,
                        BigDecimal.valueOf(0)
                )
        );

        assertInstanceOf(SQLException.class, ex.getMostSpecificCause());

        SQLException sqlException = (SQLException) ex.getMostSpecificCause();

        assertEquals("P1004", sqlException.getSQLState());
        assertTrue(ex.getMessage().contains("Amount cannot be zero."));
    }
}
