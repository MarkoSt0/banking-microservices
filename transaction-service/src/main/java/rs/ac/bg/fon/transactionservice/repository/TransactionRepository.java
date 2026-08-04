package rs.ac.bg.fon.transactionservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final JdbcTemplate template;

    public UUID createTransaction(UUID senderId, UUID receiverId, BigDecimal amount){
        return template.execute(
            (CallableStatementCreator) con -> {
                CallableStatement cs = con.prepareCall(
                    "CALL api.spr_create_transaction(?,?,?,?)"
                );
                cs.setObject(1,senderId, Types.OTHER);
                cs.setObject(2, receiverId, Types.OTHER);
                cs.setBigDecimal(3, amount);
                cs.registerOutParameter(4, Types.OTHER);
                return cs;
            },
            cs -> {
                cs.execute();
                return UUID.fromString(cs.getObject(4).toString());
            }
        );
    }

    public void completeTransaction(UUID transactionId){
        template.execute(
                (CallableStatementCreator) con -> {
                    CallableStatement cs = con.prepareCall(
                            "CALL api.spr_complete_transaction(?)"
                    );
                    cs.setObject(1, transactionId, Types.OTHER);
                    return cs;
                },
                PreparedStatement::execute
        );
    }

    public void failTransaction(UUID transactionId, String failureReason){
        template.execute(
                (CallableStatementCreator) con -> {
                    CallableStatement cs = con.prepareCall(
                            "CALL api.spr_fail_transaction(?,?)"
                    );
                    cs.setObject(1, transactionId, Types.OTHER);
                    cs.setString(2, failureReason);
                    return cs;
                },
                PreparedStatement::execute
        );
    }
}
