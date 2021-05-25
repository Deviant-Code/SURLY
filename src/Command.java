// Represents a Command to be executed by the interpreter
public interface Command {
    public void execute(Environment env) throws QueryException;
}
