package digit.academy.tutorial.web.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserType {
    ADVOCATE,
    ADVOCATE_CLERK,
    SYSTEM,
    NYAY_MITRA;

    @JsonCreator
    public static UserType fromValue(String text) {
        if (text == null) {
            return null;
        }
        try {
            return UserType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Or throw an exception depending on your use case
        }
    }
}