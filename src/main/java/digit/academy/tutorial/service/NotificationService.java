package digit.academy.tutorial.service;


import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.web.models.Advocate;
import digit.models.coremodels.SMSRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {

    private final Producer producer;

    private final Configuration config;

    public void prepareEventAndSend(Advocate advocate) {
        List<SMSRequest> smsRequestList = new ArrayList<>();

        SMSRequest smsRequest = SMSRequest.builder()
                .mobileNumber(advocate.getIndividualId())
                .message(getCustomMessage(advocate.getWorkflow().getComments(),  advocate)).build();
        smsRequestList.add(smsRequest);

        for (SMSRequest sms : smsRequestList) {
            producer.push(config.getSmsNotificationTopic(), sms);
            log.info("Messages: " + sms.getMessage());
        }
    }

    private String getCustomMessage(String template, Advocate application) {
        template = template.replace("{APPNUMBER}", application.getApplicationNumber());
        return template;
    }
}