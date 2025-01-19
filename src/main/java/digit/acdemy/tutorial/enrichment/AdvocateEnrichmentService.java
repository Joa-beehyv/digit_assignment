package digit.acdemy.tutorial.enrichment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import digit.acdemy.tutorial.util.IdgenUtil;
import digit.acdemy.tutorial.util.MdmsUtil;
import digit.acdemy.tutorial.web.models.Advocate;
import digit.acdemy.tutorial.web.models.AdvocateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class AdvocateEnrichmentService {

    private final IdgenUtil idgenUtil;

    private final MdmsUtil mdmsUtil;

    JsonNode idFormatNode;
    Set<String> advocateIdFormatName;

    public List<Advocate> enrichAdvocateRequest(AdvocateRequest request) throws JsonProcessingException {
        List<Advocate> advocates = request.getAdvocates().stream()
                .map(a -> a.withId(UUID.randomUUID()))
                .toList();

        String advocateType = request.getAdvocates().get(0).getAdvocateType().toLowerCase();
        advocateIdFormatName = switch (advocateType) {
            case "advocate" -> Set.of("advocate.id");
            case "advocate_clerk" -> Set.of("advocate.clerk.id");
            default -> Set.of();
        };

        idFormatNode = mdmsUtil.fetchMdmsData(
                request.getRequestInfo(),
                request.getRequestInfo().getUserInfo().getTenantId(),
                "masters.IdFormat",
                advocateIdFormatName);

        JsonNode idFormat = idFormatNode.get("IdFormat").get("format");

        log.info("id format selected: {}", idFormat);

        List<String> advocateIdList = idgenUtil.getIdList(
                request.getRequestInfo(),
                request.getAdvocates().get(0).getTenantId(),
                idFormat.asText(),
                "",
                request.getAdvocates().size());

        int index = 0;
        for (Advocate application : advocates) {
            // Enrich audit details
            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(request.getRequestInfo().getUserInfo().getUuid())
                    .createdTime(System.currentTimeMillis())
                    .lastModifiedBy(request.getRequestInfo().getUserInfo().getUuid())
                    .lastModifiedTime(System.currentTimeMillis()).build();
            application.setAuditDetails(auditDetails);

            //Enrich application number from IDgen
            application.setApplicationNumber(advocateIdList.get(index++));
        }
        return advocates;
    }
}
