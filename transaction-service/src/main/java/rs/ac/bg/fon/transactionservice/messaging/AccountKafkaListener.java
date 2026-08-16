package rs.ac.bg.fon.transactionservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.transactionservice.dto.event.TransferFundsResult;
import rs.ac.bg.fon.transactionservice.service.TransactionService;

@RequiredArgsConstructor
@Component
public class AccountKafkaListener {

    private final TransactionService transactionService;

    @KafkaListener(
            topics = "transfer-funds-results",
            groupId = "groupId",
            containerFactory = "factory"
    )
    void listener(TransferFundsResult result){
        transactionService.processTransferResult(result);
    }
}
