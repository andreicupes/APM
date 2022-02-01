package Model.stmt;

import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IProcTable;
import Model.types.IType;
import javafx.util.Pair;

import java.util.ArrayList;

public class ProcedureStmt implements IStmt{
    private final String name;
    private final ArrayList<String> parameters;
    private final IStmt body;

    public ProcedureStmt(String _name, ArrayList<String> _v, IStmt _b) {
        name = _name;
        parameters = _v;
        body = _b;
    }

    @Override
    public String toString() {
        return "procedure " + name + "(" + parameters.toString() + ")" + body.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {

        IProcTable<String, Pair<ArrayList<String>, IStmt>> procTable = state.getProcTable();

        procTable.add(name, new Pair<>(parameters, body));
        return null;
    }
}