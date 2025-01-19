package digit.acdemy.tutorial.web.models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserType {
    private final Map<String, String> values = new HashMap<>();

    public void addValue(String code) {
        values.put(code, code);
    }

    public String getValue(String code) {
        return values.get(code);
    }

    public Map<String, String> getAllValues() {
        return values;
    }
}
