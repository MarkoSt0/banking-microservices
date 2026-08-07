package rs.ac.bg.fon.transactionservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic transferFundsCommands(){
        System.out.println("Creating topic bean");
        return TopicBuilder.name("transfer-funds-commands").build();
    }
}
