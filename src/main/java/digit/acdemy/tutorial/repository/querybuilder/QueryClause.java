package digit.acdemy.tutorial.repository.querybuilder;

import java.util.List;

public class QueryClause {
    StringBuilder appendClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        return query.append(preparedStmtList.isEmpty() ? " WHERE " : " AND ");
    }
}
