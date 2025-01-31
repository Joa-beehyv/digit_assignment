package digit.academy.tutorial.web.models.enums;

import lombok.Getter;

public enum AdvocateType {
    ADVOCATE_GENERAL("Advocate General", "Advocate General", true),
    PUBLIC_PROSECUTOR("Public Prosecutor", "Public Prosecutor", true),
    STANDING_COUNSEL("Standing Counsel", "Standing Counsel", true);

    // Getter for code
    @Getter
    private final String code;
    // Getter for displayName
    @Getter
    private final String displayName;
    private final boolean isActive;

    // Constructor to initialize the enum
    AdvocateType(String code, String displayName, boolean isActive) {
        this.code = code;
        this.displayName = displayName;
        this.isActive = isActive;
    }

    // Getter for isActive
    public boolean isActive() {
        return isActive;
    }

    // Static method to get enum by code
    public static AdvocateType fromCode(String code) {
        for (AdvocateType type : AdvocateType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
