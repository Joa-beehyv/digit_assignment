package digit.academy.tutorial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.RedisConfig;
import digit.academy.tutorial.enrichment.AdvocateEnrichmentService;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateResponse;
import lombok.SneakyThrows;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.common.contract.workflow.State;
import org.egov.common.validator.Validator;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvocateServiceTest {

    @InjectMocks
    private AdvocateRepository repository;
    @Mock
    private AdvocateEnrichmentService enrichmentService;
    @Mock
    private List<Validator<AdvocateRequest, Advocate>> validators;
    @Mock
    private WorkflowService workflowService;
    @Mock
    private RedisService redisService;
    @Mock
    private ProcessInstanceService processInstanceService;
    @Spy
    @InjectMocks
    AdvocateService advocateService;
    @Mock
    private RedisConfig redisConfig;
    @Mock
    RedisConnectionFactory redisConnectionFactory;
    @Mock
    RedisTemplate redisTemplate;
    @InjectMocks
    private  ObjectMapper mapper;
    @Mock
    private Validator<AdvocateRequest, Advocate> validator1;

    @Mock
    private Validator<AdvocateRequest, Advocate> validator2;

    @BeforeEach
    void setup() {
        validators = List.of(validator1, validator2);
    }


    Workflow workflow = Workflow.builder()
            .action("REGISTER")
            .comments("Advocate Registration")
            .documents(List.of(Document.builder()
                    .fileStore("511d7d16-953b-421f-bccf-da865c015a20")
                    .documentType("image/png")
                    .build()))
            .build();

    Advocate advocate =  Advocate.builder()
            .advocateType("PROSECUTOR")
            .barRegistrationNumber("BCI/MH/2023/12345")
            .tenantId("default")
            .individualId("INDV1")
            .workflow(workflow)
            .build();

    AdvocateRequest advocateRequest = AdvocateRequest.builder()
            .advocates(Collections.singletonList(advocate))
            .build();

    ProcessInstance processInstance = ProcessInstance.builder()
            .businessId("ADVOCATE-REGISTER")
            .action("REGISTER")
            .state(State.builder().build())
            .build();

    ProcessInstanceResponse processInstanceResponse = ProcessInstanceResponse.builder()
            .processInstances(Collections.singletonList(processInstance))
            .build();

    @SneakyThrows
    @Test
    void advocateCreateShouldSucceed() {

        String id = UUID.randomUUID().toString();
        String appNumber = "ADVOC_000025_2025";

        when(enrichmentService.enrichAdvocateRequest(advocateRequest))
                .thenReturn(advocateRequest
                        .withAdvocates(List.of(advocate.withId(id)
                                .withApplicationNumber(appNumber))));

        doNothing().when(redisService).saveData(any(), any());

        doReturn(AdvocateResponse.builder().build()).when(advocateService)
                .updateWorkflowStatus(any());

        advocateService.create(advocateRequest);

        ArgumentCaptor<AdvocateRequest> captor = ArgumentCaptor.forClass(AdvocateRequest.class);

        verify(advocateService, times(1))
                .create(captor.capture());

       verify(advocateService, times(1))
                .updateWorkflowStatus(captor.capture());

        assertEquals(captor.getValue(), advocateRequest);
    }
}
