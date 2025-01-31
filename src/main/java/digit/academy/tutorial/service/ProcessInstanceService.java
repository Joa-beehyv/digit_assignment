package digit.academy.tutorial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.AdvocateConfiguration;
import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.repository.ServiceRequestRepository;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.workflow.ProcessInstanceSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.ProcessInstanceRequest;
import org.egov.common.contract.workflow.ProcessInstanceResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProcessInstanceService {

    private final ServiceRequestRepository repository;

    private final Configuration config;

    private final AdvocateConfiguration advocateConfig;

    private final ObjectMapper mapper;

    public ProcessInstanceResponse callWorkFlow(ProcessInstanceRequest workflowReq) {
        ProcessInstanceResponse response = null;
        StringBuilder url = new StringBuilder(config.getWfHost().concat(config.getWfTransitionPath()));
        Object optional = repository.fetchResult(url, workflowReq);
        response = mapper.convertValue(optional, ProcessInstanceResponse.class);
        return response;
    }

    public ProcessInstance createProcessInstance(Advocate advocate) {
        ProcessInstance processInstance = ProcessInstance.builder()
                .businessId(advocate.getApplicationNumber())
                .action(advocate.getWorkflow().getAction())
                .moduleName(advocateConfig.getModuleName())
                .tenantId(advocate.getTenantId())
                .businessService(advocateConfig.getBusinessService())
                .documents(advocate.getWorkflow().getDocuments())
                .comment(advocate.getWorkflow().getComments())
                .build();

        if (!CollectionUtils.isEmpty(advocate.getWorkflow().getAssignes())) {
            List<User> users = new ArrayList<>();

            advocate.getWorkflow().getAssignes().forEach(uuid -> {
                User user = new User();
                user.setUuid(uuid);
                users.add(user);
            });

            processInstance.setAssignes(users);
        }
        return processInstance;
    }

    public ProcessInstance getCurrentProcessState(ProcessInstanceSearchCriteria criteria) {
        String tenantId = criteria.getTenantId();
        StringBuilder url = createProcessSearchURL(tenantId, criteria.getBusinessIds());

        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder()
                .requestInfo(criteria.getRequestInfo()).build();

        Object result = repository.fetchResult(url, requestInfoWrapper);
        ProcessInstanceResponse response = null;
        try {
            response = mapper.convertValue(result, ProcessInstanceResponse.class);
        } catch (IllegalArgumentException e) {
            throw new CustomException("PARSING ERROR", "Failed to parse response of process instance search");
        }

        if (CollectionUtils.isEmpty(response.getProcessInstances()))
            throw new CustomException("PROCESS_INSTANCE_NOT_FOUND", "The process instance is not found");

        return response.getProcessInstances().get(0);
    }

    StringBuilder createProcessSearchURL(String tenantId, List<String> businessIds) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        String ids = String.join(",", businessIds);

        log.info("ids: {}", ids);

        url.append(config.getWfProcessSearch());
        url.append("?tenantId=");
        url.append(tenantId);
        url.append("&businessIds=");
        url.append(ids);
        return url;
    }
}
