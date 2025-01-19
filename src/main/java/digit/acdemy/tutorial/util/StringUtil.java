package digit.acdemy.tutorial.util;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class StringUtil {

    public BiPredicate<String, Integer> hasExactlyNDigits = (input, length) ->
            input != null && input.matches("^\\d{" + length + "}$");

    public Predicate<String> isNullOrEmpty = ObjectUtils::isEmpty;

    public BiPredicate<String, String> matchesPattern = (input, regex) ->
            input != null && Pattern.compile(regex).matcher(input).matches();

}
