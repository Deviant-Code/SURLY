// Command to remove a relation from the database
public class CommandDestroy implements Command {
    private String name;

    public CommandDestroy(String name) {
        this.name = name;
    }

    public void execute(Environment env) throws QueryException {
        // Make sure we are not destroying a variable
        if(env.containsVar(name)){
            throw new QueryException("Cannot destroy temporary relation " + name);
        }

        // Remove the variable from the database
        env.getDatabase().remove(name);
        
        if (env.isVerbose()) {
            System.out.println("Destroying relation: " + name);
        }
    }
}
