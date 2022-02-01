package Model.adt;

public interface IStack<T> {

    T pop() throws Exception;

    void push(T v);

    boolean isEmpty();

    String toString();
    String toFile();
    IStack<T> deepCopy();
}

