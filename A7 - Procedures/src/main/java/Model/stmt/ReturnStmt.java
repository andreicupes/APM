package Model.stmt;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;

public class ReturnStmt implements IStmt{
    @Override
    public String toString() {
        return "return";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {

        state.getSymTableStack().pop();
        return null;
    }
}