package digit.acdemy.tutorial.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Data
public class AdvocateConfiguration {

    @Value("${adv.kafka.create.topic}")
    private final String saveAdvocateTopic;

    @Value("${adv.kafka.create.topic}")
    private final String updateAdvocateTopic;
}
