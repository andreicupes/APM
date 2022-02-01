package Model.stmt;


import Exceptions.DeclareException;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;
import Model.value.IValue;

public class VarDeclStmt implements IStmt {
    private String name;
    private IType type;

    public VarDeclStmt(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type + " " + this.name;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        if (symTbl.isDefined(name)) throw new DeclareException("variable already defined");

        symTbl.add(name, type.defaultValue());

        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        typeEnv.add(name, type);
        return typeEnv;
    }
}
