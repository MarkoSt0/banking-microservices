package rs.ac.bg.fon.transactionservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import rs.ac.bg.fon.transactionservice.dto.command.TransferFundsCommand;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
public class TransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(KafkaTemplate<String, TransferFundsCommand> kafkaTemplate){
		return args -> {
			TransferFundsCommand command = new TransferFundsCommand(
					UUID.randomUUID(),
					UUID.randomUUID(),
					BigDecimal.valueOf(10000)
			);
			kafkaTemplate.send("transfer-funds-commands", command);
		};
	}
}
