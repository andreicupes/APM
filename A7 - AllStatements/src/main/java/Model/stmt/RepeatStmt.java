package Model.stmt;

import Exceptions.ExpException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyHeap;
import Model.exp.Exp;
import Model.exp.NotExp;
import Model.types.BoolType;
import Model.types.IType;
import Model.value.IValue;

public class RepeatStmt implements IStmt{
    private Exp exp;
    private IStmt stmt;

    public RepeatStmt(Exp _exp, IStmt _stmt){
        exp = _exp;
        stmt = _stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        IStack<IStmt> stack = state.getStack();

        IStmt repeatStmt = new CompStmt(stmt, new WhileStmt(exp, stmt, false));

        stack.push(repeatStmt);
        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType type1;

        type1 = exp.typeCheck(typeEnv);

        if (type1.equals(new BoolType())) {
            stmt.typeCheck(typeEnv);
        } else throw new ExpException("Expression type must be boolean");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "repeat " + stmt.toString() + " until " + exp.toString();
    }
}
