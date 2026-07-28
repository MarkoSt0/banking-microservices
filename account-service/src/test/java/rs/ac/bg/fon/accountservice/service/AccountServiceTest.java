package rs.ac.bg.fon.accountservice.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@RequiredArgsConstructor
public class AccountServiceTest {
    private final AccountService accountService;
}
