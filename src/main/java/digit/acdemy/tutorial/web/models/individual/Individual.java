package digit.acdemy.tutorial.web.models.individual;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

/**
 * A representation of an Individual.
 */
@ApiModel(description = "A representation of an Individual.")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2022-12-27T11:47:19.561+05:30")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Individual {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("individualId")
    private String individualId = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("userId")
    private String userId = null;

    @JsonProperty("userUuid")
    private String userUuid = null;

    @JsonProperty("name")
    private Name name = null;

    @JsonProperty("dateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth = null;

    @JsonProperty("gender")
    private String gender = null;

    @JsonProperty("mobileNumber")
    private String mobileNumber = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("pincode")
    private String pinCode = null;

    @JsonProperty("state")
    private String state = null;

    @JsonProperty("district")
    private String district = null;

    @JsonProperty("town")
    private String town = null;

    @JsonProperty("street_name")
    private String streetName = null;

    @JsonProperty("building_name")
    private String buildingName = null;

    @JsonProperty("userDetails")
    private UserDetails userDetails;
}
