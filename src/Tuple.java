import java.util.LinkedList;
import java.util.Iterator;
import java.lang.Iterable;

public class Tuple implements Iterable<Value> {
    private LinkedList<Value> values;

    public Tuple(LinkedList<Value> values) {
        this.values = values;
    }

    public boolean containsValue(int index, Value value) throws QueryException {
        return values.get(index).valueEq(value);
    }

    public Value get(int index){
        return values.get(index);
    }

    public LinkedList<Value> getValues(){
        return this.values;
    }

    public Iterator<Value> iterator() {
        return values.iterator();
    }
}
