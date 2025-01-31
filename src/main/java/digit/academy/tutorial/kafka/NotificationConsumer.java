package digit.academy.tutorial.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.service.NotificationService;
import digit.academy.tutorial.web.models.Advocate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class NotificationConsumer {

    private final ObjectMapper mapper;

    private final NotificationService notificationService;

    @KafkaListener(topics = {"${adv.kafka.create.topic}"})
    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            Advocate advocate = mapper.convertValue(record, Advocate.class);
            log.info("consumed topic: {}", advocate);
            notificationService.prepareEventAndSend(advocate);

        } catch (final Exception e) {
            log.error("Error while listening to value: " + record + " on topic: " + topic + ": ", e);
        }
    }
}