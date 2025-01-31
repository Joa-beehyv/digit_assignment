package digit.academy.tutorial.repository.querybuilder;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PreparedStatementBuilder implements PreparedStatementSetter {

    private final List<Object> parameters;

    @Override
    public void setValues(@NotNull PreparedStatement ps) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            ps.setObject(i + 1, parameters.get(i));
        }
    }
}
