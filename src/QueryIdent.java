// Represents a relation identified by a name
public class QueryIdent implements Query {
    private String name;

    public QueryIdent(String name) {
        this.name = name;
    }

    public Relation evaluate(Environment env) throws QueryException {
        // Simply get the relation
        return env.getRelation(name);
    }
}

