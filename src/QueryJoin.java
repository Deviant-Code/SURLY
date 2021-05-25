import java.util.LinkedList;
import java.util.Iterator;
import java.lang.Iterable;

// Represents a JOIN query
public class QueryJoin implements Query {
    private Query leftQuery;
    private Query rightQuery;
    private Condition condition;

    public QueryJoin(Query left, Query right, Condition condition) {
        this.leftQuery = left;
        this.rightQuery = right;
        this.condition = condition;
    }

    public Relation evaluate(Environment env) throws QueryException {
        Relation leftR = leftQuery.evaluate(env);
        Relation rightR = rightQuery.evaluate(env);

        LinkedList<Tuple> leftTuples = leftR.getTuples();
        LinkedList<Tuple> rightTuples = rightR.getTuples();
        LinkedList<Tuple> joinTuples = new LinkedList<Tuple>();

        LinkedList<Attribute> joinAttr = new LinkedList<Attribute>();
        LinkedList<Attribute> leftAttr = leftR.getSchema().getList();
        LinkedList<Attribute> rightAttr = rightR.getSchema().getList();

        //Joins the schema from left and right, checking for duplicates
        //If duplicate is found we identify the Attribute by <Relation>.<AttrName>
        for (Attribute lAttr : leftAttr){
            for (Attribute rAttr : rightAttr) {
                if (lAttr.name.equalsIgnoreCase(rAttr.name)) {
                    lAttr.setQualifier();
                }
            }
            joinAttr.add(lAttr);
        }

        for(Attribute rAttr : rightAttr){
            for (Attribute lAttr : leftAttr) {
                if (rAttr.name.equalsIgnoreCase(lAttr.name)) {
                    rAttr.setQualifier();
                }
            }
            joinAttr.add(rAttr);
        }

        Schema joinSchema = new Schema("$TEMP_JOIN$",joinAttr);

        //Cartesian Product left and right relation for linked list joinTuples
        for(Tuple lTuple: leftTuples){
            for(Tuple rTuple: rightTuples){
                LinkedList<Value> joinVals = new LinkedList<Value>();
                joinVals.addAll(lTuple.getValues());
                joinVals.addAll(rTuple.getValues());
                Tuple newTuple = new Tuple(joinVals);
                joinTuples.add(newTuple);
            }
        }

        Relation newRelation = new Relation(joinSchema);
        for(Tuple tuple : joinTuples){
            if(condition.evaluate(joinSchema, tuple)){
                newRelation.addTuple(tuple);
            }
        }

        return newRelation;
    }
}
