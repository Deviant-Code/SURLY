import java.util.Iterator;

// Iterator that allows peeking. Don't know why the Java STL doesn't have this
public interface Peekable<T> extends Iterator<T> {
    T peek();
}
