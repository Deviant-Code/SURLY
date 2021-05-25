import java.util.List;
import java.util.LinkedList;

// A RELATION command, creates a new relation in the database
public class CommandRelation implements Command {
    private String title;
    private List<AttrInfo> attrInfoList;

    public CommandRelation(String title, List<AttrInfo> attrInfoList) {
        this.title = title;
        this.attrInfoList = attrInfoList;
    }

    public void execute(Environment env) throws QueryException {
        LinkedList<Attribute> attrs = new LinkedList<Attribute>();

        // Build each attribute
        for (AttrInfo info : attrInfoList) {
            attrs.add(new Attribute(info.name,
                                    title,
                                    Type.fromString(info.type, info.size)));
        }

        // Add the relation to the database
        env.getDatabase().add(title, new Schema(title, attrs));

        if (env.isVerbose()) {
            System.out.println("Creating " + title + " with " + attrInfoList.size()
                    + " attributes");
        }
    }

    // Information for a single attribute
    public static class AttrInfo {
        public final String name;
        public final String type;
        public final int size;

        public AttrInfo(String name, String type, int size) {
            this.name = name;
            this.type = type;
            this.size = size;
        }
    }
}
