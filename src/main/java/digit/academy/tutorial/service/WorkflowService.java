package digit.academy.tutorial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.AdvocateConfiguration;
import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.repository.ServiceRequestRepository;
import digit.academy.tutorial.web.models.Advocate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.workflow.*;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Component
@Slf4j
public class WorkflowService {

    private final ServiceRequestRepository repository;

    private final ObjectMapper mapper;

    private final AdvocateConfiguration advocateConfig;

    private final Configuration config;


    private BusinessService getBusinessService(Advocate application, RequestInfo requestInfo) {
        String tenantId = application.getTenantId();

        StringBuilder url = createBusinessSearchURLWithParams(tenantId);

        RequestInfoWrapper requestInfoWrapper
                = RequestInfoWrapper.builder().requestInfo(requestInfo).build();

        Object result = repository.fetchResult(url, requestInfoWrapper);
        BusinessServiceResponse response = null;
        try {
            response = mapper.convertValue(result, BusinessServiceResponse.class);
        } catch (IllegalArgumentException e) {
            throw new CustomException("PARSING ERROR", "Failed to parse response of workflow business service search");
        }

        if (CollectionUtils.isEmpty(response.getBusinessServices()))
            throw new CustomException("BUSINESSSERVICE_NOT_FOUND", "The businessService " + "BTR" + " is not found");

        return response.getBusinessServices().get(0);
    }

    //gets business service using provided parameters
    public ProcessInstance getBusinessService(RequestInfo requestInfo, String tenantId, String businessId) {

        RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();

        StringBuilder url = createBusinessSearchURLWithParams(tenantId);

        Object res = repository.fetchResult(url, requestInfoWrapper);
        ProcessInstanceResponse response = null;

        try {
            response = mapper.convertValue(res, ProcessInstanceResponse.class);
        } catch (Exception e) {
            throw new CustomException("PARSING_ERROR", "Failed to parse workflow search response");
        }

        if (response != null && !CollectionUtils.isEmpty(response.getProcessInstances()) && response.getProcessInstances().get(0) != null)
            return response.getProcessInstances().get(0);

        return null;
    }

    StringBuilder createBusinessSearchURLWithParams(String tenantId) {
        StringBuilder url = new StringBuilder(config.getWfHost());
        url.append(config.getWfBusinessServiceSearchPath());
        url.append("?tenantId=");
        url.append(tenantId);
        url.append("&businessServices=");
        url.append(advocateConfig.getBusinessService());
        return url;
    }
}