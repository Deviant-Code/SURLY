import java.util.Iterator;
import java.util.LinkedList;


public class DataBase{
    public static final int MAX_NAME_SIZE = 30;

    private LinkedList<Relation> relations;
    private Relation catalog;
    private int count;

    //Constructor for new database, catalog
    public DataBase(){
        relations = new LinkedList<Relation>();
        catalog = buildCatalog();
        relations.add(catalog);
    }

    //Constructs a new instance of database catalog
    private Relation buildCatalog() {
        LinkedList<Attribute> catSchema = new LinkedList<Attribute>();
        catSchema.add(new Attribute("relation", "Catalog",
                    Type.fromString("CHAR", MAX_NAME_SIZE)));
        catSchema.add(new Attribute("attribute", "Catalog",
                    Type.fromString("CHAR", MAX_NAME_SIZE)));
        catSchema.add(new Attribute("type", "Catalog",
                    Type.fromString("CHAR", MAX_NAME_SIZE)));

        return new Relation("Catalog", new Schema("Catalog", catSchema));
    }

    //Retrieve a relation from database using the relation name
    //Throw an exception if relation does not exist in d.b.
    public Relation getRelation(String relationName) throws QueryException {
        Iterator<Relation> it = relations.iterator();

        Relation r;
        while(it.hasNext()) {
            r = it.next();
            if (r.getName().equalsIgnoreCase(relationName)) {
                return r;
            }

        }
        throw new QueryException(relationName + " does not exist");
    }

    //Add a new relation to the database
    //Throws an exception if relation already exists in d.b.
    public void add(String relationName, Schema schema) throws QueryException {
        //If not already in database
        if(!isRelation(relationName)){
            relations.add(new Relation(relationName, schema));

            for (Attribute attr : schema) {
                LinkedList<Value> catValues = new LinkedList<Value>();

                catValues.add(
                        new Value(
                            catalog.getSchema().getAttribute(0),
                            relationName
                            ));
                catValues.add(
                        new Value(
                            catalog.getSchema().getAttribute(1), 
                            attr.name
                            ));
                catValues.add(
                        new Value(
                            catalog.getSchema().getAttribute(2),
                            attr.type.toString()
                            ));
                catalog.addTuple(new Tuple(catValues));
            }
        } else {
            throw new QueryException("Attempted to add " + relationName
                    + ", which is already in the database");
        }
    }

    //Check if a relation is already in the database
    public Boolean isRelation(String relationName){
        Iterator<Relation> it = relations.iterator();

        Relation r;
        while(it.hasNext()) {
            r = it.next();
            if (r.getName().equalsIgnoreCase(relationName)) {
                return true;
            }
        }
        return false;
    }

    //Remove a relation from the database
    //Throws exception if relation does not exist
    public void remove(String relationName) throws QueryException {
        Iterator<Relation> it = relations.iterator();

        Relation r;
        while(it.hasNext()) {
            r = it.next();
            if(r.getName().equalsIgnoreCase(relationName)){
                relations.remove(r); // remove from database
                
                Value v = new Value(Type.fromString("CHAR", MAX_NAME_SIZE),
                        relationName);
                catalog.removeByValue("RELATION", v);

                this.count--;
                return;
            }
        }

        throw new QueryException("Attempted to remove " + relationName
                + ", which is not in the database");
    }

}
