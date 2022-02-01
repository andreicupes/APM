package Model.stmt;

import Exceptions.ExpException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyHeap;
import Model.exp.Exp;
import Model.exp.ValueExp;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;

public class WaitStmt implements IStmt{
    private final Exp exp;

    public WaitStmt(Exp _exp) {
        exp = _exp;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType type = exp.typeCheck(typeEnv);

        if (!type.equals(new IntType())) {
            throw new ExpException("Expression value is not integer!");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        IStack<IStmt> stack = state.getStack();

        IntValue value = (IntValue)exp.eval(symTbl, heapTbl);

        if (value.getValue() > 0) {
            stack.push(new CompStmt(new PrintStmt(new ValueExp(new IntValue(value.getValue()))),new WaitStmt(new ValueExp(new IntValue(value.getValue() - 1)))));
        }

        return null;
    }

    @Override
    public String toString(){
        return "wait ("+ exp.toString()+")";
    }
}
