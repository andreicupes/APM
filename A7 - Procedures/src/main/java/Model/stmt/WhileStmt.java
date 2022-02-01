package Model.stmt;

import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyHeap;
import Model.exp.Exp;
import Model.types.BoolType;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class WhileStmt implements IStmt{

    private Exp exp;
    private IStmt stmt;

    public WhileStmt(Exp e, IStmt s){
        this.exp = e;
        this.stmt = s;
    }

    @Override
    public String toString() {
        return "while ( " + exp.toString() + " ) { " + stmt.toString()+" }";
    }


    @Override
    public PrgState execute(PrgState state) throws Exception {

        IStack<IStmt> stk = state.getStack();
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer,IValue> heapTbl = (MyHeap<Integer,IValue>) state.getHeapTable();
        IValue val = exp.eval(symTbl,heapTbl);

        if ((val.getType()).equals(new BoolType()))
        {
            BoolValue cond = (BoolValue) val;
            if (cond.getValue()) {
                IStmt copyWhile = new WhileStmt(exp, stmt);
                stk.push(copyWhile);
                stk.push(stmt);
            }
        } else {
            throw new TypeException("Conditional expression is not a boolean");
        }
        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {

        IType typeExp = exp.typeCheck(typeEnv);
        if (typeExp.equals(new BoolType())) {
            stmt.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else throw new TypeException("The conditional expression is not boolean");
    }
}
