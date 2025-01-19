package digit.acdemy.tutorial.web.models.individual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import digit.models.coremodels.user.enums.UserType;
import jakarta.validation.Valid;
import org.egov.common.models.core.Role;

import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class UserDetails {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("roles")
    private @Valid List<Role> roles;
    @JsonProperty("type")
    private UserType userType;

}
