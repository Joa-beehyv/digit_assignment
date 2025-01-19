package digit.acdemy.tutorial.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import digit.acdemy.tutorial.web.models.individual.Individual;
import lombok.*;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * Advocate
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-19T00:25:41.754980941+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class Advocate {

    @JsonProperty("id")
    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull
    @Size(min = 2, max = 128)
    private String tenantId = null;

    @JsonProperty("applicationNumber")
    @Size(min = 2, max = 64)
    private String applicationNumber = null;

    @JsonProperty("barRegistrationNumber")
    @Size(min = 2, max = 64)
    private String barRegistrationNumber = null;

    @JsonProperty("advocateType")
    @NotNull
    @Size(min = 2, max = 64)
    private String advocateType = null;

    @JsonProperty("organisationID")
    @Valid
    private UUID organisationID = null;

    @JsonProperty("individualId")
    private Individual individualId = null;

    @JsonProperty("isActive")
    private Boolean isActive = true;

    @JsonProperty("workflow")
    @Valid
    private Workflow workflow = null;

    @JsonProperty("documents")
    @Valid
    private List<Document> documents = null;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;


    public Advocate addDocumentsItem(Document documentsItem) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(documentsItem);
        return this;
    }

}
