// Represents a query to be evaluated into a concrete Relation
public interface Query {
    Relation evaluate(Environment env) throws QueryException;
}
