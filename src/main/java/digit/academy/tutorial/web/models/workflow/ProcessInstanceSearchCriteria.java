package digit.academy.tutorial.web.models.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Builder
@Data
public class ProcessInstanceSearchCriteria {

    @JsonProperty("requestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("status")
    private List<String> status;

    @JsonProperty("businessIds")
    private List<@Size(min=4) String> businessIds;

    @JsonProperty("assignee")
    private String  assignee = null;

    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("history")
    private Boolean history = false;

    @JsonProperty("fromDate")
    private Long fromDate = null;

    @JsonProperty("toDate")
    private Long toDate = null;

    @JsonProperty("sortOrder")
    private String sortOrder;


    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("limit")
    private Integer limit;

    @JsonProperty("businessService")
    private String businessService;

    @JsonProperty("moduleName")
    private String moduleName;

    @JsonIgnore
    private Boolean isNearingSlaCount;

    @JsonIgnore
    private List<String> tenantSpecifiStatus;

    @JsonIgnore
    private List<String> multipleAssignees;

    @JsonIgnore
    private List<String> statesToIgnore;

    @JsonIgnore
    private Boolean isEscalatedCount;

    @JsonIgnore
    private Boolean isAssignedToMeCount;

    @JsonIgnore
    private List<String> statusesIrrespectiveOfTenant;

    @JsonIgnore
    private Long slotPercentageSlaLimit;




    public Boolean isNull(){
        if(this.getBusinessIds()==null && this.getIds()==null && this.getAssignee()==null &&
                this.getStatus()==null)
            return true;
        else return false;
    }



}
