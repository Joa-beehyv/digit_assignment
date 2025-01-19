package digit.acdemy.tutorial.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.acdemy.tutorial.service.notifcation.NotificationService;
import digit.acdemy.tutorial.web.models.AdvocateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;

@Component
@Slf4j
public class NotificationConsumer {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = {"${btr.kafka.create.topic}"})
    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        try {

            AdvocateRequest request = mapper.convertValue(record, AdvocateRequest.class);
            //log.info(request.toString());
            notificationService.notify(Collections.singletonList(request.getAdvocates()));

        } catch (final Exception e) {

            log.error("Error while listening to value: " + record + " on topic: " + topic + ": ", e);
        }
    }

}