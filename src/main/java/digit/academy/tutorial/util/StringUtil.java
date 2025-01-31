package digit.acdemy.tutorial.util;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class StringUtil {

    // check if the input is  exactly  'n' length characters
    public BiPredicate<String, Integer> hasExactlyNDigits = (input, length) ->
            input != null && input.matches("^\\d{" + length + "}$");

    // check if the input is  exactly  'n' length characters
    public BiPredicate<String, Integer> hasExactlyNAlphanumericChars = (input, length) ->
            input != null && input.matches("^[a-zA-Z0-9]{" + length + "}$");


    // check if the input is alphanumeric
    public Predicate<String> isAlphanumeric = input ->
            input != null && input.matches("^[a-zA-Z0-9]+$");

    public Predicate<String> isNullOrEmpty = ObjectUtils::isEmpty;

    public BiPredicate<String, String> matchesPattern = (input, regex) ->
            input != null && Pattern.compile(regex).matcher(input).matches();

}
