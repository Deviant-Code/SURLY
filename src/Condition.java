// Represents a Condition to be checked on a Tuple
public interface Condition {
    boolean evaluate(Schema schema, Tuple tuple) throws QueryException;
}
