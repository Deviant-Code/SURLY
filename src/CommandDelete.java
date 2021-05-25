import java.util.ListIterator;

// Command to delete rows from a relation
public class CommandDelete implements Command {
    private String relationName;
    private Condition condition;

    public CommandDelete(String relationName, Condition condition) {
        this.relationName = relationName;
        this.condition = condition;
    }

    public void execute(Environment env) throws QueryException {
        // Get the relation from the database, not the environment (to avoid
        // a DELETE on variables)
        Relation r = env.getDatabase().getRelation(relationName);
        ListIterator<Tuple> iter = r.getTuples().listIterator();

        int count = 0;

        // Iterate through each Tuple and remove it if the condition is
        // satisfied
        while (iter.hasNext()) {
            if (condition.evaluate(r.getSchema(), iter.next())) {
                iter.remove();
                count++;
            }
        }

        if (env.isVerbose()) {
            System.out.println("Deleting " + count + " tuples from "
                    + relationName);
        }
    }
}
