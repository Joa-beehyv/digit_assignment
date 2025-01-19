package digit.acdemy.tutorial.repository.querybuilder;

import digit.acdemy.tutorial.web.models.AdvocateSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdvocateQueryBuilder
        implements QueryBuilder<AdvocateSearchCriteria, QueryPair<String, PreparedStatementSetter>> {

    private final QueryClause addClause;

    private static final String BASE_QUERY = """
                SELECT 
                    id, 
                    tenantid, 
                    applicationnumber, 
                    barregistrationnumber, 
                    advocatetype, 
                    organisationid, 
                    individualid, 
                    isactive, 
                    workflow, 
                    documents, 
                    auditdetails, 
                    additionaldetails
            """;

    private static final String FROM_TABLES
            = " FROM advocate ";

    @Override
    public QueryPair<String, PreparedStatementSetter> build(AdvocateSearchCriteria criteria) {
        StringBuilder query = new StringBuilder(BASE_QUERY);
        query.append(FROM_TABLES);

        List<Object> preparedStmtList = new ArrayList<>();

        if (!ObjectUtils.isEmpty(criteria.getId())) {
            query = addClause.appendClauseIfRequired(query, preparedStmtList);
            query.append(" id = ? ");
            preparedStmtList.add(criteria.getId());
        }

        if (!ObjectUtils.isEmpty(criteria.getApplicationNumber())) {
            query = addClause.appendClauseIfRequired(query, preparedStmtList);
            query.append(" applicationnumber = ? ");
            preparedStmtList.add(criteria.getApplicationNumber());
        }

        return new QueryPair<>(query.toString(), new PreparedStatementBuilder(preparedStmtList));
    }
}
