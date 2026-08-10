package rs.ac.bg.fon.transactionservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.transactionservice.dto.command.TransferFundsCommand;
import rs.ac.bg.fon.transactionservice.dto.request.CreateTransactionRequest;
import rs.ac.bg.fon.transactionservice.dto.response.CreateTransactionResponse;
import rs.ac.bg.fon.transactionservice.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, TransferFundsCommand> kafkaTemplate;

    public CreateTransactionResponse executeTransaction(CreateTransactionRequest request){
        UUID senderAccountId = request.senderAccountId();
        UUID receiverAccountId = request.receiverAccountId();
        BigDecimal amount = request.amount();

        System.out.println("Sender: " + senderAccountId + ", receiver: " + receiverAccountId + ", " + amount);

        // 1. Create pending transaction
        UUID transactionId = createTransaction(senderAccountId, receiverAccountId, amount);

        // 2. Sent command to topic(Kafka)
        TransferFundsCommand command = new TransferFundsCommand(
                senderAccountId,
                receiverAccountId,
                amount
        );
        kafkaTemplate.send("transfer-funds-commands", command);

        // 3. Check result from kafka

        // 4. Complete or fail transaction
        completeTransaction(transactionId);

        // 5. Send command to Notification Microservice

        return new CreateTransactionResponse(transactionId);
    }

    private UUID createTransaction(UUID senderAccountId, UUID receiverAccountId, BigDecimal amount){
        return transactionRepository.createTransaction(
                senderAccountId,
                receiverAccountId,
                amount
        );
    }

    private void completeTransaction(UUID transactionId){
        transactionRepository.completeTransaction(transactionId);
    }
}
