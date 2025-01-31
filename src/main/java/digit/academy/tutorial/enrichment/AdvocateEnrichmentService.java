package digit.academy.tutorial.enrichment;

import com.fasterxml.jackson.core.JsonProcessingException;
import digit.academy.tutorial.util.IdgenUtil;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Slf4j
@Component
public class AdvocateEnrichmentService {

    private final IdgenUtil idgenUtil;

    public AdvocateRequest enrichAdvocateRequest(AdvocateRequest request) {

        // Enrich advocate IDs
        List<Advocate> advocates = request.getAdvocates().stream()
                .map(a -> a.withId(UUID.randomUUID().toString()))
                .toList();

        // Determine  advocate idName based on user type
        String userType = request.getRequestInfo().getUserInfo().getType();
        String advocateIdName = switch (userType == null ? "" : userType.toLowerCase()) {
            case "advocate" -> "advocate.id";
            case "advocate_clerk" -> "advocate.clerk.id";
            default -> throw new IllegalArgumentException("Unsupported user type: " + userType);
        };

        // Generate advocate IDs
        List<String> advocateIdList = idgenUtil.getIdList(
                request.getRequestInfo(),
                advocates.get(0).getTenantId(),
                advocateIdName,
                "",
                advocates.size()
        );

        // Enrich advocates with audit details and application numbers
        IntStream.range(0, advocates.size()).forEach(index -> {
            Advocate advocate = advocates.get(index);

            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(request.getRequestInfo().getUserInfo().getUuid())
                    .createdTime(System.currentTimeMillis())
                    .lastModifiedBy(request.getRequestInfo().getUserInfo().getUuid())
                    .lastModifiedTime(System.currentTimeMillis())
                    .build();
            advocate.setAuditDetails(auditDetails);

            advocate.setApplicationNumber(advocateIdList.get(index));
        });

        request.setAdvocates(advocates);
        return request;
    }
}
