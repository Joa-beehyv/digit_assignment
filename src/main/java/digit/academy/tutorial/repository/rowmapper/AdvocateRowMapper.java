package digit.academy.tutorial.repository.rowmapper;

import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateResponse;
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
public class AdvocateRowMapper implements ResultSetExtractor<List<Advocate>> {

    @Override
    public List<Advocate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Advocate> advocates = new ArrayList<>();
        while (rs.next()) {
            Advocate advocate = Advocate.builder()
                    .advocateType(rs.getString("advocatetype"))
                    .id(rs.getString("id"))
                    .applicationNumber(rs.getString("applicationnumber"))
                    .tenantId(rs.getString("tenantid"))
                    .barRegistrationNumber(rs.getString("barregistrationnumber"))
                    .organisationId(rs.getString("organisationid"))
                    .isActive(rs.getBoolean("isactive"))
                    .build();

            advocates.add(advocate);
        }
        return advocates;
    }
}
