package Model.exp;

import Exceptions.ExpException;
import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;

public class MULExp extends Exp {

    private Exp exp1;
    private Exp exp2;

    public MULExp(Exp e1, Exp e2) {
        exp1 = e1;
        exp2 = e2;
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typexp1 = exp1.typeCheck(typeEnv);
        IType typexp2 = exp2.typeCheck(typeEnv);

        if (typexp1.equals(new IntType()) && typexp2.equals(new IntType()))
            return new IntType();
        else
            throw new ExpException("Expressions should be int");
    }

    @Override
    public IValue eval(IDict<String, IValue> table, MyHeap<Integer, IValue> heap) throws Exception {
        return new ArithExp('-', new ArithExp('*', exp1, exp2), new ArithExp('+', exp1, exp2)).eval(table, heap);
    }

    @Override
    public String toString() {
        return "MUL(" + exp1.toString() + "," + exp2 + ")";
    }
}