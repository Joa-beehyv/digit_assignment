package digit.acdemy.tutorial.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.acdemy.tutorial.config.Configuration;
import digit.acdemy.tutorial.web.models.MdmsCriteriaReqV2;
import digit.acdemy.tutorial.web.models.MdmsCriteriaV2;
import digit.acdemy.tutorial.web.models.MdmsResponseV2;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static digit.acdemy.tutorial.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_MDMS;


@Slf4j
@Component
public class MdmsUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Configuration configs;

    public JsonNode fetchMdmsData(RequestInfo requestInfo,
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
            response = restTemplate.postForObject(uri.toString(), mdmsCriteriaReq, Map.class);
            mdmsResponse = mapper.convertValue(response, MdmsResponseV2.class);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_FROM_MDMS, e);
        }

        return mapper.treeToValue(mdmsResponse.getMdms().get(0).getData(), JsonNode.class);
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