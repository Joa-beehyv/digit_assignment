package digit.academy.tutorial.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

/**
 * AdvocateRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-19T00:25:41.754980941+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class AdvocateRequest {

    @JsonProperty("requestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("advocates")
    @Valid
    private List<Advocate> advocates = null;

    public AdvocateRequest addAdvocatesItem(Advocate advocatesItem) {
        if (this.advocates == null) {
            this.advocates = new ArrayList<>();
        }
        this.advocates.add(advocatesItem);
        return this;
    }
}
