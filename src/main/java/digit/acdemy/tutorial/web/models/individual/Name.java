package digit.acdemy.tutorial.web.models.individual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Validated
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Builder
public class Name {
    @JsonProperty("first_name")
    private String firstName = null;
    @JsonProperty("middle_name")
    private String middleName = null;
    @JsonProperty("last_name")
    private String lastName = null;
}
