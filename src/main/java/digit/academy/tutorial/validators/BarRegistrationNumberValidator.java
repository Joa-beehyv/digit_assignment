package digit.academy.tutorial.validators;

import digit.academy.tutorial.config.AdvocateConfiguration;
import digit.acdemy.tutorial.util.StringUtil;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
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
import java.util.function.Predicate;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Component
@Slf4j
public class BarRegistrationNumberValidator implements Validator<AdvocateRequest, Advocate> {

    private final AdvocateConfiguration configuration;

    private final StringUtil stringUtil;

    @Override
    public Map<Advocate, List<Error>> validate(AdvocateRequest request) {
        log.info("validating aadhaarNumber");
        List<Error> errors = new ArrayList<>();
        Map<Advocate, List<Error>> errorDetailsMap = new HashMap<>();
        List<Advocate> advocates = request.getAdvocates();
        advocates.forEach(advocate -> {
            if (!isValidBarRegNumber.test(
                    advocate.getBarRegistrationNumber())) {
                errors.add(Error.builder()
                        .errorMessage("Bar Registration pattern is invalid")
                        .errorCode("BAR_REG_NUMBER_ERROR")
                        .type(Error.ErrorType.NON_RECOVERABLE)
                        .exception(new CustomException("INVALID_LENGTH", "Bar Registration number Length is invalid"))
                        .build());
                errorDetailsMap.put(advocate, errors);
            }
        });

        return errorDetailsMap;
    }

    public static final Predicate<String> isValidBarRegNumber = (barRegNumber) ->
                    Pattern.matches("^BCI/[A-Z]{2,3}/\\d{4}/\\d{5,}$", barRegNumber);

}
