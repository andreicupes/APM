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
    private Boolean check;

    public WhileStmt(Exp e, IStmt s){
        this.exp = e;
        this.stmt = s;
        check = true;
    }

    public WhileStmt(Exp _exp, IStmt _stmt, Boolean _check) {
        exp = _exp;
        stmt = _stmt;
        check = _check;
    }


    @Override
    public String toString() {
        return "while ( " + exp.toString() + " ) { " + stmt.toString()+" }";
    }


    @Override
    public PrgState execute(PrgState state) throws Exception {

        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        IStack<IStmt> stack = state.getStack();

        IValue expEval = exp.eval(symTbl, heapTbl);

        if (expEval.getType().equals(new BoolType())) {
            BoolValue bV = (BoolValue) expEval;
            if (bV.getValue() == check) {
                IStmt copyWhile = new WhileStmt(exp, stmt);
                stack.push(copyWhile);
                stack.push(stmt);
            }
        } else throw new TypeException("Condition exp is not a boolean;)");

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
