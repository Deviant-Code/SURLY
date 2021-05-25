import java.util.Map;
import java.util.HashMap;

public class Environment {
    private DataBase database;
    private Map<String, Relation> nameTable;

    private boolean verbose;

    public Environment(DataBase db) {
        useDatabase(db);
        nameTable = new HashMap<String, Relation>();
        verbose = true;
    }

    public Relation getRelation(String name) throws QueryException {
        if (containsVar(name))
            return nameTable.get(name);
        else
            return database.getRelation(name);
    }

    public void addVar(String name, Relation r) {
        nameTable.put(name, r);
    }

    public boolean containsVar(String name) {
        return nameTable.containsKey(name);
    }

    public void useDatabase(DataBase db) {
        database = db;
    }

    public DataBase getDatabase() {
        return database;
    }

    public void setVerbosity(boolean v) {
        this.verbose = v;
    }

    public boolean isVerbose() {
        return verbose;
    }
}

