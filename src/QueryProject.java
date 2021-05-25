import java.util.List;
import java.util.LinkedList;

public class QueryProject implements Query {
    private List<AttrExpr> attributes;
    private Query query;

    public QueryProject(Query query, List<AttrExpr> attrs) {
        this.attributes = attrs;
        this.query = query;
    }

    public Relation evaluate(Environment env) throws QueryException {
        if (attributes.isEmpty())
            throw new QueryException("No attributes specified to project");

        Relation r = query.evaluate(env);
        Schema oldSchema = r.getSchema();

        if (attributes.get(0).isAggregate()) {
            return evaluateAggregate(r);
        }
        else {
            return evaluateNormal(r);
        }
    }

    private Relation evaluateAggregate(Relation r) throws QueryException {
        LinkedList<Attribute> newAttrs = new LinkedList<Attribute>();
        LinkedList<Value> newValues = new LinkedList<Value>();

        for (AttrExpr attrExpr : attributes) {
            if (!attrExpr.isAggregate()) {
                throw new QueryException(
                        "Attempted to project non aggregate attribute '"
                        + attrExpr.getName() + "' in aggregate project");
            }

            newAttrs.add(new Attribute(attrExpr.getName(), 
                         Type.fromString("NUM", 4)));
            newValues.add(((AttrExprAggr)attrExpr).aggregate(r));
        }

        Relation newRelation = new Relation(new Schema(r.getName(), newAttrs));
        newRelation.addTuple(new Tuple(newValues));
        return newRelation;
    }

    private Relation evaluateNormal(Relation r) throws QueryException {
        Schema oldSchema = r.getSchema();

        LinkedList<Attribute> newAttr = new LinkedList<Attribute>();
        LinkedList<Integer> attrIndex = new LinkedList<Integer>();
        LinkedList<Tuple> tuples = r.getTuples();

        for(AttrExpr attribute: attributes){
            if (attribute.isAggregate()) {
                throw new QueryException(
                        "Attempted to project aggregate attribute '"
                        + attribute.getName()
                        + "' in non-aggrevate project");
            }

            int index = oldSchema.indexOf(attribute.getName());
            attrIndex.add(index);
            newAttr.add(oldSchema.getAttribute(index));
        }

        Schema newSchema = new Schema("", newAttr);
        Relation newRelation = new Relation(newSchema);

        for(Tuple tuple: tuples){
            LinkedList<Value> values = new LinkedList<Value>();
            for(int index : attrIndex){
                values.add(tuple.get(index));
            }

            // Check for duplicate tuples, if not duplicate add to new
            // project relation (temporary)
            if(!newRelation.contains(values)){
                Tuple newTuple = new Tuple(values);
                newRelation.addTuple(newTuple);
            }
        }

        return newRelation;
    }
}

