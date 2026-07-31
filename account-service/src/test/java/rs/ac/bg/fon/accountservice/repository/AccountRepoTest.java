package rs.ac.bg.fon.accountservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import rs.ac.bg.fon.accountservice.TestcontainersConfiguration;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class AccountRepoTest {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldCreateAccount(){
        UUID id = accountRepo.createAccount(
                "Test Person",
                BigDecimal.valueOf(12000),
                "RSD"
        );

        Assertions.assertNotNull(id);
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

        Assertions.assertNotNull(id1);
        Assertions.assertNotNull(id2);
        Assertions.assertNotEquals(id1, id2);
    }

//    Better solution is to use error code in database and to check that code

    @Test
    void shouldThrowWhenCreatingEmptyOwnerName(){
        DataAccessException ex = Assertions.assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        "",
                        BigDecimal.valueOf(12000),
                        "RSD"
                )
        );

        Assertions.assertTrue(ex.getMessage().contains("Owner name cannot be empty."));
    }

    @Test
    void shouldThrowWhenCreatingNullOwnerName(){
        DataAccessException ex = Assertions.assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        null,
                        BigDecimal.valueOf(12000),
                        "RSD"
                )
        );

        Assertions.assertTrue(ex.getMessage().contains("Owner name cannot be empty."));
    }

    @Test
    void shouldThrowWhenCreatingNullBalance(){
        DataAccessException ex = Assertions.assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        "Test Person",
                        null,
                        "RSD"
                )
        );

        Assertions.assertTrue(ex.getMessage().contains("Initial balance cannot be null."));
    }

    @Test
    void shouldThrowWhenCreatingNegativeBalance(){
        DataAccessException ex = Assertions.assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        "Test Person",
                        BigDecimal.valueOf(-1000),
                        "USD"
                )
        );

        Assertions.assertTrue(ex.getMessage().contains("Initial balance cannot be negative."));
    }

    @Test
    void shouldThrowWhenCreatingNullCurrency(){
        DataAccessException ex = Assertions.assertThrows(
                DataAccessException.class,
                () -> accountRepo.createAccount(
                        "Test Person",
                        BigDecimal.valueOf(12000),
                        null
                )
        );

        Assertions.assertTrue(ex.getMessage().contains("Currency cannot be null."));
    }
}
