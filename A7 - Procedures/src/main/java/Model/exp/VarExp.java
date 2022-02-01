package Model.exp;

import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.types.IType;
import Model.value.IValue;
import Model.value.IntValue;

public class VarExp extends Exp {
    private String id;

    public VarExp(String id) {
        this.id = id;
    }
    @Override
    public IValue eval(IDict<String, IValue> symTable, MyHeap<Integer,IValue> heapTable) {
        return symTable.lookup(id);
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws Exception {
        return typeEnv.lookup(id);
    }

    public String toString() {
        return id;
    }

}
