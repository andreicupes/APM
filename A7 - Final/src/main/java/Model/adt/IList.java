package Model.adt;

import java.util.Iterator;
import java.util.List;

public interface IList<T> {
    void add(T v);

    T pop() throws Exception;

    String toString();
    String toFile();
    T getValue(int position);

    boolean empty();

    void clear();

    int size();
    T get(int index) throws Exception;
    Iterator<T> getIterator();
    List<T> getAll();
    T peek() throws Exception;
}
