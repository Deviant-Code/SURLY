public class AttrExpr {
    private String name;

    public AttrExpr(String name) {
        this.name = name;
    }

    public Value fetch(Schema schema, Tuple tuple) throws QueryException {
        return tuple.get(schema.indexOf(name));
    }

    public String getName() {
        return name;
    }

    public boolean isAggregate() {
        return false;
    }
}
