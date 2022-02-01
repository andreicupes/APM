package Model.exp;

import Exceptions.HeapException;
import Exceptions.TypeException;
import Model.types.IType;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;
import Model.adt.*;

public class ReadHeapExp extends Exp {

    private Exp exp;

    public ReadHeapExp(Exp _exp) {
        this.exp = _exp;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, MyHeap<Integer,IValue> heapTable) throws Exception {

        IValue expEval = exp.eval(symTable, heapTable);

        if (expEval.getType() instanceof RefType) {
            int address = ((RefValue) expEval).getAddr();
            if (heapTable.isDefined(address)) {
                return heapTable.lookup(address);
            } else throw new HeapException("Uninitialized address memory");

        } else throw new TypeException("The variable must be of type RefValue");
    }
    public IType typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typ = exp.typeCheck(typeEnv);

        if (typ instanceof RefType) {
            RefType reft = (RefType) typ;
            return reft.getInner();
        } else throw new TypeException("The variable must be of REfType");
    }


    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }
}
