import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import java.lang.StringBuilder;

public class Relation{
    private String name;
    private LinkedList<Tuple> tuples;
    private Schema schema;

    public Relation(Schema schema) {
        this(schema.getName(), schema);
    }

    //Constructs a new relation given its name and schema
    public Relation(String name, Schema schema){
        this.name = name;
        this.tuples = new LinkedList<Tuple>();
        this.schema = schema;
    }


    public String getName() {
        return this.name;
    }

    public Schema getSchema() {
        return this.schema;
    }

    public void addTuple(Tuple tuple){
        tuples.add(tuple);
    }

    public void removeByValue(String attr, Value value) throws QueryException {
        ListIterator<Tuple> iter = tuples.listIterator();

        int index = schema.indexOf(attr);

        while (iter.hasNext()) {
            if (iter.next().containsValue(index, value))
                iter.remove();
        }
    }

    public LinkedList<Tuple> getTuples(){
        return this.tuples;
    }

    public boolean isTemp() {
        return false;
    }

    public boolean contains(LinkedList<Value> values) throws QueryException {
        for(Tuple tuple : tuples){
            Boolean flag = false;
            for(int i = 0; i < values.size(); i++){
                if(!tuple.containsValue(i, values.get(i))){
                    flag = false;
                    break;
                }
                flag = true;
            }
            if(flag){
                return true;
            }
        }
        return false;
    }

    // It's big but it's fairly simple.
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        LinkedList<Integer> largestList = new LinkedList<Integer>();

        final int PADDING = 2;

        for (Attribute attr : schema) {
            largestList.add(attr.toString().length());
        }

        ListIterator<Integer> largestIter;

        for (Tuple tuple : tuples) {
            largestIter = largestList.listIterator();

            for (Value value : tuple) {
                int largest = largestIter.next();
                int vSize = value.toString().length();

                if (vSize > largest) {
                    largestIter.set(vSize);
                }
            }
        }

        largestIter = largestList.listIterator();

        for (Attribute attr : schema) {
            builder.append(attr.toString());

            int printed = attr.toString().length();
            int size = largestIter.next();

            for (int i = 0; i < size - printed + PADDING; i++) {
                builder.append(" ");
            }
        }

        for (Tuple tuple : tuples) {
            builder.append("\n");

            largestIter = largestList.listIterator();

            for (Value value : tuple) {
                int size = largestIter.next();

                String vstr = value.toString();
                int printed = vstr.length();

                if (value.type.isChar()) {
                    builder.append(vstr);
                }

                for (int i = 0; i < size - printed; i++) {
                    builder.append(" ");
                }

                if (value.type.isNum()) {
                    builder.append(vstr);
                }

                for (int i = 0; i < PADDING; i++) {
                    builder.append(" ");
                }
            }
        }

        return builder.toString();
    }
}
