// Represents a direct comparison of two values
public class ConditionCompare implements Condition {
    private String left;
    private String right;
    private String op;

    public static final String EQ = "=";
    public static final String NEQ = "!=";
    public static final String GT = ">";
    public static final String LT = "<";
    public static final String GTEQ = ">=";
    public static final String LTEQ = "<=";

    public ConditionCompare(String left, String right, String op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public boolean evaluate(Schema schema, Tuple tuple) throws QueryException {
        Value lValue = null;
        Value rValue = null;
        Type type = null;

        // Attempt to find the identifiers in the schema, and if so, get the
        // appropriate type to compare on
        if (schema.contains(left)) {
            int index = schema.indexOf(left);
            type = schema.getAttribute(index).type;
            lValue = tuple.get(index);
        }
        if (schema.contains(right)) {
            int index = schema.indexOf(right);
            type = schema.getAttribute(index).type;
            rValue = tuple.get(index);
        }

        // We were able to get a concrete type to compare on
        if (type != null) {

            // Construct any static Values
            if (lValue == null) {
                lValue = new Value(type, left);
            }
            else if (rValue == null) {
                rValue = new Value(type, right);
            }

            // Compare the values on the operator
            return handleOp(lValue, rValue);
        }
        else {
            throw new QueryException(
                    "Attempting comparison of two static values: '" + left
                    + "' and '" + right + "'");
        }
    }

    // Tests the values on the operator
    private boolean handleOp(Value lValue, Value rValue)
            throws QueryException {
        if (op.equals(EQ)) {
            return lValue.valueEq(rValue);
        }
        else if (op.equals(NEQ)) {
            return lValue.valueNeq(rValue);
        }
        else if (op.equals(GT)) {
            return lValue.valueGt(rValue);
        }
        else if (op.equals(LT)) {
            return lValue.valueLt(rValue);
        }
        else if (op.equals(GTEQ)) {
            return lValue.valueGteq(rValue);
        }
        else if (op.equals(LTEQ)) {
            return lValue.valueLteq(rValue);
        }
        else {
            throw new QueryException("Invalid comparison operator: '"
                    + op + "'");
        }
    }
}
