package digit.acdemy.tutorial.service.notifcation;


import digit.acdemy.tutorial.config.Configuration;
import digit.acdemy.tutorial.kafka.Producer;
import digit.models.coremodels.SMSRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {

    private final  Producer producer;

    private final Configuration config;

    public void notify(List<Object> messages) {
        messages.forEach(sms -> producer.push(config.getSmsNotificationTopic(), sms));
    }
}