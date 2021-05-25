// Empty condition for queries which allow it
public class ConditionEmpty implements Condition {
    public boolean evaluate(Schema schema, Tuple tuple) throws QueryException {
        return true;
    }
}
