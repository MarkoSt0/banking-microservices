package rs.ac.bg.fon.accountservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.accountservice.dto.command.TransferFundsCommand;
import rs.ac.bg.fon.accountservice.service.AccountService;

@RequiredArgsConstructor
@Component
public class AccountKafkaListener {

    private final AccountService accountService;

    @KafkaListener(
            topics = "transfer-funds-commands",
            groupId = "groupId",
            containerFactory = "factory"
    )
    void listener(TransferFundsCommand command){
        accountService.processTransfer(command);
    }
}
