import java.util.List;

// Represents a SELECT query
public class QuerySelect implements Query {
    private Query query;
    private Condition condition;

    public QuerySelect(Query query, Condition condition) {
        this.query = query;
        this.condition = condition;
    }

    public Relation evaluate(Environment env) throws QueryException {
        // Evaluate inner query
        Relation r = query.evaluate(env);

        Relation newRelation = new Relation(r.getSchema());

        List<Tuple> tuples = r.getTuples();

        // Only pick queries which satisfy the condition
        for(Tuple tuple : tuples){
            if(condition.evaluate(r.getSchema(), tuple)){
                newRelation.addTuple(tuple);
            }
        }

        return newRelation;
    }
}
