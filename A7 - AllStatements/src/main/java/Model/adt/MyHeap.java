package Model.adt;

import Model.value.IValue;

import java.util.Collection;

public class MyHeap<T1,T2> extends MyDict<T1,T2> {

    public Integer address;


    public MyHeap() {
        super();
        this.address = 1;
    }

    public void getNextFreeAddress() {
        address++;
    }


    public int add(T2 v2) throws Exception {
        if (!this.isDefined((T1)address)) {
            int copy_freePos = address;
            this.dictionary.put((T1)address, v2);
            getNextFreeAddress();
            return copy_freePos;
        }
        return 0;
    }


}
