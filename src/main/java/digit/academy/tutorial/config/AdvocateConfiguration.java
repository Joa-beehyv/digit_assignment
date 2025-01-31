package digit.academy.tutorial.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
@Data
public class AdvocateConfiguration {

    @Value("${adv.kafka.create.topic}")
    private String saveAdvocateTopic;

    @Value("${adv.kafka.update.topic}")
    private String updateAdvocateTopic;

    @Value("${app.business.service}")
    private String businessService;

    @Value("${app.module.name}")
    private String moduleName;

    @Value("${adv.bar.registration.len}")
    private int barRegistrationLength;
}
