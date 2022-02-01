package Model.stmt;

import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.types.IType;

public class CompStmt implements IStmt {
    private IStmt first;
    private IStmt second;

    public CompStmt(IStmt v, IStmt v1) {
        this.first = v;
        this.second = v1;
    }

    public IStmt getFirst(){
        return this.first;
    }

    public IStmt getSecond(){
        return this.second;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ";" + second.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IStack<IStmt> stk = state.getStack();
        stk.push(second);
        stk.push(first);
        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {

        return second.typeCheck(first.typeCheck(typeEnv));
    }
}
