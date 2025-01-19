package digit.acdemy.tutorial.repository.querybuilder;

public interface QueryBuilder<T, V> {
    V build(T criteria);
}