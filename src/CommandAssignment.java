// Represents a variable assignment
public class CommandAssignment implements Command {
    private String name;
    private Query query;

    public CommandAssignment(String name, Query query) {
        this.name = name;
        this.query = query;
    }

    public void execute(Environment env) throws QueryException {
        // Evaluate the query
        Relation r = query.evaluate(env);
        // Add the variable to the environment
        env.addVar(name, r);

        if (env.isVerbose()) {
            System.out.println("Assigning variable '" + name + "'");
        }
    }
}

