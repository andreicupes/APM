package Model.exp;

import Exceptions.*;
import Model.*;
import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class NotExp extends Exp{
    private Exp exp;

    public NotExp(Exp e){exp=e;}

    @Override
    public IValue eval(IDict<String, IValue> table, MyHeap<Integer,IValue> heap) throws Exception {
        BoolValue b=(BoolValue)exp.eval(table, heap);
        return !b.getValue() ? new BoolValue(true) : new BoolValue(false);
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws Exception {
        return exp.typeCheck(typeEnv);
    }

    @Override
    public String toString() {
        return "!("+exp.toString()+")";
    }
}