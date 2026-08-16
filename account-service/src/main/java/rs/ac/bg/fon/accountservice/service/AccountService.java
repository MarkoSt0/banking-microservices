package rs.ac.bg.fon.accountservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.accountservice.dto.command.TransferFundsCommand;
import rs.ac.bg.fon.accountservice.dto.event.TransferFundsResult;
import rs.ac.bg.fon.accountservice.dto.request.CreateAccountRequest;
import rs.ac.bg.fon.accountservice.dto.request.UpdateBalanceRequest;
import rs.ac.bg.fon.accountservice.dto.response.AccountResponse;
import rs.ac.bg.fon.accountservice.enums.TransferStatus;
import rs.ac.bg.fon.accountservice.repository.AccountRepo;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepo accountRepo;
    private final KafkaTemplate<String, TransferFundsResult> kafkaTemplate;

    public AccountResponse createAccount(CreateAccountRequest createRequest){
        UUID uuid = accountRepo.createAccount(
                createRequest.ownerName(),
                createRequest.initialBalance(),
                createRequest.currency()
        );
        return new AccountResponse(uuid);
    }

    public void updateBalance(UpdateBalanceRequest request){
        accountRepo.updateBalance(
                request.uuid(),
                request.amount()
        );
    }

    public void processTransfer(TransferFundsCommand command) {
        deductFromSender(command);
        addToReceiver(command);

        kafkaTemplate.send(
                "transfer-funds-results",
                new TransferFundsResult(command.transactionId(), TransferStatus.SUCCESS, null)
        );
    }

    private void deductFromSender(TransferFundsCommand command){
        accountRepo.updateBalance(
                command.senderAccountId(),
                command.amount().negate()
        );
    }

    private void addToReceiver(TransferFundsCommand command){
        accountRepo.updateBalance(
                command.receiverAccountId(),
                command.amount()
        );
    }
}
