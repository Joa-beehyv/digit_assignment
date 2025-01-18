package digit.acdemy.tutorial.repository.rowmapper;

import digit.acdemy.tutorial.web.models.AdvocateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
@Slf4j
public class AdvocateRowMapper implements ResultSetExtractor<List<AdvocateResponse>> {

    @Override
    public List<AdvocateResponse> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<AdvocateResponse> advocateResponses = new ArrayList<>();
        while (rs.next()) {
            AdvocateResponse weatherSearch = AdvocateResponse.builder()
                    .advocates(List.of())
                    .build();
            advocateResponses.add(weatherSearch);
        }
        return advocateResponses;
    }
}
