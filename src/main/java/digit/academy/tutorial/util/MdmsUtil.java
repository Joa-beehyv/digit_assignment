package digit.academy.tutorial.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.Configuration;
import digit.academy.tutorial.web.models.MdmsCriteriaReqV2;
import digit.academy.tutorial.web.models.MdmsCriteriaV2;
import digit.academy.tutorial.web.models.MdmsResponseV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static digit.academy.tutorial.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_MDMS;


@Slf4j
@Component
@RequiredArgsConstructor
public class MdmsUtil {

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final Configuration configs;

    public MdmsResponseV2 fetchMdmsData(RequestInfo requestInfo,
                                  String tenantId,
                                  String schemaCode,
                                  Set<String> uniqueIdentifiers) throws JsonProcessingException {
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getMdmsHost()).append(configs.getMdmsEndPoint());
        MdmsCriteriaReqV2 mdmsCriteriaReq
                = getMdmsRequest(requestInfo, tenantId, schemaCode,uniqueIdentifiers);

        Object response = new HashMap<>();
        Integer rate = 0;
        MdmsResponseV2 mdmsResponse = new MdmsResponseV2();
        try {
            mdmsResponse = restTemplate.postForObject(uri.toString(), mdmsCriteriaReq, MdmsResponseV2.class);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_MDMS, e);
        }

        //log.info("response: {}", mdmsResponse);

        return mdmsResponse;
        //log.info(ulbToCategoryListMap.toString());
    }

    private MdmsCriteriaReqV2 getMdmsRequest(RequestInfo requestInfo, String tenantId,
                                             String schemeCode, Set<String> uniqueIdentifier) {

        MdmsCriteriaV2 mdmsCriteria = MdmsCriteriaV2.builder()
                .tenantId(tenantId.split("\\.")[0])
                .schemaCode(schemeCode)
                .uniqueIdentifiers(uniqueIdentifier)
                .build();

        return MdmsCriteriaReqV2.builder()
                .mdmsCriteria(mdmsCriteria)
                .requestInfo(requestInfo)
                .build();
    }
}