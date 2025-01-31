package digit.academy.tutorial.web.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import digit.academy.tutorial.service.AdvocateService;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateResponse;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-01-19T00:25:41.754980941+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class AdvocateApiController {

    private final AdvocateService advocateService;

    @PostMapping("/advocate/v1/_create")
    public ResponseEntity<AdvocateResponse> advocateV1CreatePost(@Valid @RequestBody AdvocateRequest request) {

        // Validate
        if (request == null || request.getAdvocates() == null || request.getAdvocates().isEmpty()) {
            throw new CustomException("Invalid Create Request","Invalid AdvocateRequest: Advocates list cannot be null or empty");
        }
        try {
            return new ResponseEntity<>(AdvocateResponse.builder()
                    .advocates(advocateService.create(request))
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new CustomException("CREATE ERROR", "Error creating Advocate");
        }
    }

    @PostMapping(value = "/advocate/v1/_search")
    public ResponseEntity<AdvocateResponse> advocateV1SearchPost(@Valid @ModelAttribute AdvocateSearchCriteria searchCriteria,
                                                                 @Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
        try {
            return new ResponseEntity<>(
                    advocateService.search(requestInfoWrapper.getRequestInfo(), searchCriteria),
                    HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Search Error", "Unable to search Advocate");
        }
    }

    @PostMapping(value = "/advocate/v1/_update")
    public ResponseEntity<AdvocateResponse> advocateV1UpdatePost(@Valid @RequestBody AdvocateRequest request) {
        try {
            return new ResponseEntity<>(
                    advocateService.update(request),
                    HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("ERROR", "Error updating Advocate workflow");
        }
    }
}
