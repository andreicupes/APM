package Model.adt;

import java.util.Collection;
import java.util.Map;

public interface ILatchTable <T1,T2> {
    void add(T1 v1, T2 v2) throws Exception;

    void update(T1 v1, T2 v2) throws Exception;

    void remove(T1 v1) throws Exception;

    T2 lookup(T1 id);

    boolean isDefined(T1 id);

    String toString();

    Collection<T2> values();

    void setContent(Map<T1, T2> content);
    Map<T1, T2> getContent();

    String toFile();

    ILatchTable<T1, T2> deepCopy();
}

