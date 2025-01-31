package digit.academy.tutorial.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.enrichment.AdvocateEnrichmentService;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateResponse;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import digit.academy.tutorial.web.models.workflow.ProcessInstanceSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.common.models.Error;
import org.egov.common.validator.Validator;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvocateService {

    private final AdvocateRepository repository;
    private final AdvocateEnrichmentService enrichmentService;
    private final List<Validator<AdvocateRequest, Advocate>> validators;
    private final WorkflowService workflowService;
    private final RedisService redisService;
    private final ProcessInstanceService processInstanceService;
    private final ObjectMapper mapper;

    public List<Advocate> create(AdvocateRequest request) {
        //validate application
        validators.forEach(validator -> {
            Map<Advocate, List<Error>> errors = validator.validate(request);
            if (!errors.isEmpty()) {
                throw new CustomException("INVALID REGISTRATION", errors.toString());
            }
        });

        //enrich application
        AdvocateRequest advocateRequest = enrichmentService.enrichAdvocateRequest(request);

        //save to redis
        advocateRequest.getAdvocates().forEach(
                a -> redisService.saveData(a.getApplicationNumber(), a));

        //create workflow
        updateWorkflowStatus(request);

        return advocateRequest.getAdvocates();
    }

    public AdvocateResponse update(AdvocateRequest request) {
        return updateWorkflowStatus(request);
    }

    AdvocateResponse updateWorkflowStatus(AdvocateRequest request) {

        //create process instance
        ProcessInstance processInstance
                = processInstanceService.createProcessInstance(request.getAdvocates().get(0));

        //update workflow state
        ProcessInstanceResponse response
                = processInstanceService.callWorkFlow(ProcessInstanceRequest.builder()
                .processInstances(Collections.singletonList(processInstance))
                .requestInfo(request.getRequestInfo())
                .build());

        processInstance = response.getProcessInstances().get(0);

        if (Boolean.TRUE.equals(processInstance.getState().getIsTerminateState())) {

            //fetch data from redis
            Advocate advocate = searchAdvocateFromCache(processInstance.getBusinessId());

            if (advocate == null) throw new CustomException("Error", "No Such user in cache");

            //Add comment
            advocate.getWorkflow().setComments(processInstance.getComment());

            //push to kafka
            repository.save(advocate);
        }
        return AdvocateResponse.builder()
                .advocates(request.getAdvocates())
                .responseInfo(ResponseInfo.builder()
                        .status("successful")
                        .build())
                .processInstance(processInstance)
                .build();
    }

    public AdvocateResponse search(RequestInfo requestInfo,
                                   AdvocateSearchCriteria searchCriteria) {

        ProcessInstanceSearchCriteria processInstanceSearchCriteria
                = createSearchCriteria(requestInfo, searchCriteria);

        ProcessInstance processInstance
                = processInstanceService.getCurrentProcessState(processInstanceSearchCriteria);

        Advocate advocate = null;

        if (Boolean.FALSE.equals(processInstance.getState().getIsTerminateState())) {
            //fetch data from redis
            advocate = searchAdvocateFromCache(processInstance.getBusinessId());

        } else {
            advocate = repository.searchAdvocate(AdvocateSearchCriteria.builder()
                    .applicationNumber(processInstance.getBusinessId())
                    .build()).get(0);
        }

        return AdvocateResponse.builder()
                .advocates(List.of(advocate))
                .responseInfo(ResponseInfo.builder()
                        .status("successful")
                        .build())
                .processInstance(processInstanceService.getCurrentProcessState(processInstanceSearchCriteria))
                .build();
    }

    ProcessInstanceRequest getProcessInstanceForAdvocate(AdvocateRequest updateRequest) {
        Advocate app = updateRequest.getAdvocates().get(0);

        ProcessInstance processInstance = processInstanceService.createProcessInstance(app);

        return ProcessInstanceRequest.builder()
                .requestInfo(updateRequest.getRequestInfo())
                .processInstances(Collections.singletonList(processInstance))
                .build();
    }

    ProcessInstanceSearchCriteria createSearchCriteria(RequestInfo requestInfo,
                                                               AdvocateSearchCriteria criteria) {
        return ProcessInstanceSearchCriteria.builder()
                .tenantId(requestInfo.getUserInfo().getTenantId())
                .businessIds(Collections.singletonList(criteria.getApplicationNumber()))
                .requestInfo(requestInfo)
                .build();
    }

    Advocate searchAdvocateFromCache(String businessId) {
        try {
            Object cachedData = redisService.getData(businessId);
            return cachedData != null ? mapper.convertValue(cachedData, Advocate.class) : null;
        } catch (IllegalArgumentException e) {
            log.error("Error deserializing Advocate from Redis for businessId: {}", businessId, e);
            throw new IllegalArgumentException("Failed to fetch advocate from cache", e);
        }
    }
}
