package digit.acdemy.tutorial.service.notifcation;

import digit.acdemy.tutorial.web.models.Advocate;
import digit.acdemy.tutorial.web.models.AdvocateRequest;
import digit.models.coremodels.SMSRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SMSNotificationService {

    private final NotificationService service;

    private static final String smsTemplate
            = "Dear Applicant your registration application has been successfully created on the system with application number - {APPNUMBER}.";

    public void prepareEventAndSend(AdvocateRequest request) {
        List<SMSRequest> smsRequestList = new ArrayList<>();
        request.getAdvocates().forEach(application -> {
            SMSRequest smsRequest = SMSRequest.builder()
                    .mobileNumber(request.getRequestInfo().getUserInfo().getMobileNumber())
                    .message(getCustomMessage(smsTemplate, application)).
                    build();
            smsRequestList.add(smsRequest);
        });
        service.notify(Collections.singletonList(smsRequestList));
    }

    private String getCustomMessage(String template, Advocate application) {
        template = template.replace("{APPNUMBER}", application.getApplicationNumber());
        return template;
    }
}
