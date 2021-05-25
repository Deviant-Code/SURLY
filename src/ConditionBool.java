// Represents a comparison of two Conditions
public class ConditionBool implements Condition {
    private Condition left;
    private Condition right;
    private String op;

    public static final String AND = "AND";
    public static final String OR = "OR";

    public ConditionBool(Condition left, Condition right, String op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public boolean evaluate(Schema schema, Tuple tuple) throws QueryException {
        if (op.equalsIgnoreCase(AND))
            return left.evaluate(schema, tuple) && right.evaluate(schema, tuple);
        else if (op.equalsIgnoreCase(OR))
            return left.evaluate(schema, tuple) || right.evaluate(schema, tuple);
        else
            throw new QueryException("Invalid boolean operator: " + op);
    }
}
