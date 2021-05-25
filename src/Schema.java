import java.util.LinkedList;
import java.util.Iterator;
import java.lang.Iterable;

public class Schema implements Iterable<Attribute> {

    private String relationName;
    private LinkedList<Attribute> attributes;

    public Schema(String name, LinkedList<Attribute> attributes){
        this.relationName = name;
        this.attributes = attributes;
    }

    public String getName(){
        return this.relationName;
    }

    public boolean contains(String name) throws QueryException {
        for (Attribute attr : attributes) {
            if (attr.nameEquals(name)) {
                return true;
            }
        }

        return false;
    }

    public int indexOf(String name) throws QueryException {
        int index = 0;

        boolean found = false;

        for (Attribute attr : attributes) {
            if (!found) {
                if (attr.nameEquals(name)) {
                    found = true;
                }
                
                index++;
            }
            else {
                if (attr.nameEquals(name)) {
                    throw new QueryException("Name qualifier required for '"
                            + name + "'");
                }
            }
        }

        if (found == true) {
            return index - 1;
        }

        throw new QueryException("Attribute " + name + " does not exist in "
                + relationName);
    }

    public Attribute getAttribute(String name) throws QueryException {
        return getAttribute(indexOf(name));
    }

    public Attribute getAttribute(int index) {
        return attributes.get(index);
    }

    public LinkedList<Attribute> getList(){
        return this.attributes;
    }
    public boolean isAttribute(int index) {
        try {
            attributes.get(index);
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
    
    public Iterator<Attribute> iterator() {
        return attributes.iterator();
    }
}
