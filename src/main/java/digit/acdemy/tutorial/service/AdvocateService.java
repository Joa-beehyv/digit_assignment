package digit.acdemy.tutorial.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import digit.acdemy.tutorial.enrichment.AdvocateEnrichmentService;
import digit.acdemy.tutorial.repository.AdvocateRepository;
import digit.acdemy.tutorial.validators.AadharNumberValidator;
import digit.acdemy.tutorial.web.models.Advocate;
import digit.acdemy.tutorial.web.models.AdvocateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdvocateService {

    private final AdvocateRepository repository;
    private final AdvocateEnrichmentService enrichmentService;
    private final AadharNumberValidator validator;
    private final WorkflowService workflowService;

    public List<Advocate> create(AdvocateRequest request) throws JsonProcessingException {
        //validate application
        //validator.validate(request);

        //enrich application
        List<Advocate> advocates = enrichmentService.enrichAdvocateRequest(request);

        //initiate work flow
        workflowService.updateWorkflowStatus(request);

        // push to persister
        return repository.save(advocates);
    }
}
