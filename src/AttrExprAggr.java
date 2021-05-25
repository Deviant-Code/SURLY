public class AttrExprAggr extends AttrExpr {
    private String fnName;

    public AttrExprAggr(String name, String fnName) {
        super(name);
        this.fnName = fnName;
    }

    public Value aggregate(Relation r) throws QueryException {
        if (fnName.equalsIgnoreCase("SUM"))
            return sum(r);
        else if (fnName.equalsIgnoreCase("COUNT"))
            return count(r);
        else if (fnName.equalsIgnoreCase("AVG"))
            return sum(r).div(count(r));
        else
            throw new QueryException("Unknown aggregate function: '"
                    + fnName + "'");
    }

    @Override
    public boolean isAggregate() {
        return true;
    }

    private Value sum(Relation r) throws QueryException {
        Schema schema = r.getSchema();
        double sum = 0;

        for (Tuple tuple : r.getTuples()) {
            sum += this.fetch(schema, tuple).getNum();
        }

        return new Value(sum);
    }

    private Value count(Relation r) throws QueryException {
        return new Value(r.getTuples().size());
    }
}

