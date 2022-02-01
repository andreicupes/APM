package Model.adt;

import Exceptions.ADTException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDict<T1, T2> implements IDict<T1, T2> {
    public Map<T1, T2> dictionary;

    public MyDict() {
        dictionary = new HashMap<T1, T2>();
    }

    @Override
    public void add(T1 v1, T2 v2) throws Exception {

        if(!isDefined(v1)){dictionary.put(v1, v2);}
        else throw new ADTException("Variable already defined!");
    }

    @Override
    public void update(T1 v1, T2 v2) throws Exception {
        if(isDefined(v1))dictionary.replace(v1, v2);
        else throw new ADTException("Variable not defined!");
    }

    @Override
    public void remove(T1 v1) throws Exception {
        if (this.dictionary.isEmpty()) {
            throw new ADTException("Dict is empty!");
        }
        dictionary.remove(v1);
    }

    @Override
    public T2 lookup(T1 id) {
        return dictionary.get(id);
    }

    @Override
    public boolean isDefined(T1 id) {
        return dictionary.containsKey(id);
    }

    @Override
    public String toString() {
        return dictionary.toString();
    }

    @Override
    public Collection<T2> values() {
        return dictionary.values();
    }

    @Override
    public void setContent(Map<T1, T2> content) {
        this.dictionary.clear();

        for (Map.Entry<T1, T2> entry: content.entrySet()) {
            this.dictionary.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Map<T1, T2> getContent() {
        return this.dictionary;
    }

    @Override
    public String toFile() {
        String result = "";
        Set<T1> keys = dictionary.keySet();

        for(T1 key : keys) {
            result = result + key + " -> " + dictionary.get(key) + "\n";
        }

        return result;
    }

    @Override
    public IDict<T1, T2> deepCopy() {
        MyDict<T1, T2> dictCopy = new MyDict<T1, T2>();
        dictCopy.setContent(this.getContent());

        return dictCopy;
    }
}
