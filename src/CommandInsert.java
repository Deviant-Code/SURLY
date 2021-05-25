import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

// A command to insert values into a relation
public class CommandInsert implements Command {
    private String relationName;
    private List<String> insertList;

    public CommandInsert(String relationName, List<String> insertList) {
        this.relationName = relationName;
        this.insertList = insertList;
    }

    public void execute(Environment env) throws QueryException {
        LinkedList<Value> values = new LinkedList<Value>();
        Relation relation = env.getDatabase().getRelation(relationName);

        Iterator<String> valueIter = insertList.iterator();

        for (Attribute attr : relation.getSchema()) {
            // Ensure that we have enough values for an insertion
            if (!valueIter.hasNext())
                throw new QueryException("Not enough values provided to "
                        + "insert into " + relationName);

            values.add(new Value(attr, valueIter.next()));
        }

        // Check if we have too many values for an insertion
        if (valueIter.hasNext())
            throw new QueryException("Too many values provided to insert into "
                    + relationName);

        //Checks for duplicate tuple, if unique add to database
        if(!relation.contains(values)){
            Tuple tuple = new Tuple(values);
            relation.addTuple(tuple);
        }


        if (env.isVerbose()) {
            System.out.println("Inserting " + insertList.size()
                    + " attributes to " + relationName);
        }
    }
}
