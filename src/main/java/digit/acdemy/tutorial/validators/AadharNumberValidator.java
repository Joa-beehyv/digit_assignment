package digit.acdemy.tutorial.validators;

import digit.acdemy.tutorial.config.Configuration;
import digit.acdemy.tutorial.util.StringUtil;
import digit.acdemy.tutorial.web.models.Advocate;
import digit.acdemy.tutorial.web.models.AdvocateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.models.Error;
import org.egov.common.validator.Validator;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Component
@Slf4j
public class AadharNumberValidator implements Validator<AdvocateRequest, Advocate> {

    private final Configuration configuration;

    private final StringUtil stringUtil;

    @Override
    public Map<Advocate, List<Error>> validate(AdvocateRequest request) {
        log.info("validating aadhaarNumber");
        List<Error> errors = new ArrayList<>();
        Map<Advocate, List<Error>> errorDetailsMap = new HashMap<>();
        List<Advocate> advocates = request.getAdvocates();
        advocates.forEach(advocate -> {
            if (stringUtil.hasExactlyNDigits.test(advocate.getIndividualId(), 12)) {
                errors.add(Error.builder()
                        .errorMessage("Invalid Aadhaar Number")
                        .errorCode("INVALID_AADHAAR")
                        .type(Error.ErrorType.NON_RECOVERABLE)
                        .exception(new CustomException("INVALID_AADHAAR", "Invalid Aadhaar Number")).build());

                errorDetailsMap.put(advocate, errors);
            }

            if(stringUtil.isNullOrEmpty.test(advocate.getApplicationNumber())) {
                errors.add(Error.builder()
                        .errorMessage("Invalid Aadhaar Number")
                        .errorCode("INVALID_AADHAAR")
                        .type(Error.ErrorType.NON_RECOVERABLE)
                        .exception(new CustomException("INVALID_AADHAAR", "Invalid Aadhaar Number")).build());

                errorDetailsMap.put(advocate, errors);
            }
        });

        return errorDetailsMap;
    }
}
