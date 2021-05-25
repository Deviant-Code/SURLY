import java.util.Queue;

// Command to print out relations
public class CommandPrint implements Command {
    Queue<String> printQueue;

    public CommandPrint(Queue<String> printQueue) {
        this.printQueue = printQueue;
    }

    public void execute(Environment env) throws QueryException {
        System.out.print("Printing " + printQueue.size() + " relation(s):\n");

        for (String relationName : printQueue) {
            // Try and catch here so that non-existent relations do not
            // cause the print to fail
            try {
                Relation r = env.getRelation(relationName);
                System.out.println(relationName + ":");
                System.out.println(r.toString() + "\n");
            } catch (QueryException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
