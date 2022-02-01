package Model.exp;

import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.types.*;
import Model.value.IValue;
import Model.value.IntValue;

public class ValueExp extends Exp {
    private final IValue val;

    public ValueExp(IValue _val) {
        this.val = _val;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, MyHeap<Integer,IValue> heapTable) throws Exception {
        if (val.getType().equals(new IntType()) || val.getType().equals(new BoolType()) || val.getType().equals(new StringType())
                || val.getType() instanceof RefType) {
            return val;
        }
        throw new TypeException("Unknown type specified.\n");
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws Exception {
        return val.getType();
    }

    @Override
    public String toString() {
        return this.val.toString();
    }
}
