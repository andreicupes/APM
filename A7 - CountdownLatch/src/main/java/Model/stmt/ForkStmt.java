package Model.stmt;

import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyStack;
import Model.types.IType;

public class ForkStmt implements IStmt{
    private final IStmt stmt;

    public ForkStmt(IStmt _stmt) {
        stmt = _stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {

        IStack<IStmt> newStack = new MyStack<>();
        newStack.push(stmt);

        return new PrgState(
                newStack, state.getSymTable().deepCopy(), state.getOutput(),
                stmt, state.getFileTable(), state.getHeapTable(), state.getLatchTable());
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
