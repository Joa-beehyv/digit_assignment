package digit.academy.tutorial.web.models.individual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import digit.models.coremodels.AuditDetails;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class Skill {
    @JsonProperty("id")
    private String id = null;
    @JsonProperty("clientReferenceId")
    private String clientReferenceId = null;
    @JsonProperty("individualId")
    private String individualId = null;
    @JsonProperty("type")
    private String type = null;
    @JsonProperty("level")
    private String level = null;
    @JsonProperty("experience")
    private String experience = null;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
    @JsonProperty("auditDetails")
    private @Valid AuditDetails auditDetails;

}
