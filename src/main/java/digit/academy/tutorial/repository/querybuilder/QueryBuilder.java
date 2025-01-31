package digit.academy.tutorial.repository.querybuilder;

public interface QueryBuilder<T, V> {
    V build(T criteria);
}