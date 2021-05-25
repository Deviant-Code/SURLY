public class QueryException extends Exception {
    public QueryException(String msg) {
        super("Error: " + msg);
    }
}
