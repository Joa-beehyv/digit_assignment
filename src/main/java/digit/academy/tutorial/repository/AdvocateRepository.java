package digit.academy.tutorial.repository;


import digit.academy.tutorial.config.AdvocateConfiguration;
import digit.academy.tutorial.kafka.Producer;
import digit.academy.tutorial.repository.querybuilder.AdvocateQueryBuilder;
import digit.academy.tutorial.repository.querybuilder.QueryPair;
import digit.academy.tutorial.repository.rowmapper.AdvocateRowMapper;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AdvocateRepository {

    private final AdvocateQueryBuilder queryBuilder;

    private final JdbcTemplate jdbcTemplate;

    private final AdvocateRowMapper rowMapper;

    private final Producer producer;

    private final AdvocateConfiguration advocateConfiguration;

    public List<Advocate> searchAdvocate(AdvocateSearchCriteria searchCriteria) {

        QueryPair<String, PreparedStatementSetter> query = queryBuilder.build(searchCriteria);

        log.info(" query: {}, params: {}", query.query(), searchCriteria);

        return jdbcTemplate.query(query.query(), query.params(), rowMapper);
    }

    public List<Advocate> save(Advocate objects) {
        this.producer.push(advocateConfiguration.getSaveAdvocateTopic(), objects);
        log.info("{}, Pushed to kafka", objects);
        return Collections.singletonList(objects);
    }
}