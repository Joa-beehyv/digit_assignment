package digit.academy.tutorial.repository.querybuilder;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryClause {
    StringBuilder appendClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        return query.append(preparedStmtList.isEmpty() ? " WHERE " : " AND ");
    }
}
