package Model.exp;

import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.types.IType;
import Model.value.IValue;

public abstract class Exp {

    public abstract IValue eval(IDict<String, IValue> symTable, MyHeap<Integer,IValue> heapTable) throws Exception;
    public abstract IType typeCheck(IDict<String, IType> typeEnv) throws Exception;
    public abstract String toString();
}
