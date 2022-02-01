package Model.stmt;

import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;

public interface IStmt {
    public PrgState execute(PrgState state) throws Exception;
    IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception;
    public String toString();
}
