package digit.acdemy.tutorial.repository;


import digit.acdemy.tutorial.config.AdvocateConfiguration;
import digit.acdemy.tutorial.config.Configuration;
import digit.acdemy.tutorial.repository.querybuilder.AdvocateQueryBuilder;
import digit.acdemy.tutorial.repository.querybuilder.QueryPair;
import digit.acdemy.tutorial.repository.rowmapper.AdvocateRowMapper;
import digit.acdemy.tutorial.web.models.Advocate;
import digit.acdemy.tutorial.web.models.AdvocateResponse;
import digit.acdemy.tutorial.web.models.AdvocateSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.producer.Producer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AdvocateRepository {

    private final AdvocateQueryBuilder queryBuilder;

    private final JdbcTemplate jdbcTemplate;

    private final AdvocateRowMapper rowMapper;

    private  final Producer producer;

    private AdvocateConfiguration advocateConfiguration;

    public List<AdvocateResponse> searchAdvocate(AdvocateSearchCriteria searchCriteria) {

        QueryPair<String, PreparedStatementSetter> query = queryBuilder.build(searchCriteria);

        log.info(" query: {}, params: {}", query.query(), query.params());

        return jdbcTemplate.query(query.query(), query.params(), rowMapper);
    }

    public List<Advocate> save(List<Advocate> objects) {
        this.producer.push(advocateConfiguration.getSaveAdvocateTopic(), objects);
        log.info("Pushed to kafka");
        return objects;
    }
}