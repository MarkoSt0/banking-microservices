package rs.ac.bg.fon.accountservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepo {
    private final JdbcTemplate template;

    public UUID createAccount(String ownerName, BigDecimal initialBalance, String currency){

        return template.execute(
                (CallableStatementCreator) con -> {
                    CallableStatement cs = con.prepareCall(
                            "CALL api.create_account(?, ?, ?, ?)"
                    );

                    cs.setString(1, ownerName);
                    cs.setBigDecimal(2, initialBalance);
                    cs.setString(3, currency);

                    cs.registerOutParameter(4, Types.OTHER);

                    return cs;
                },
                cs -> {
                    cs.execute();
                    return UUID.fromString(cs.getObject(4).toString());
                }
        );
    }

}
