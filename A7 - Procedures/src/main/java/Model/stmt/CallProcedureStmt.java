package Model.stmt;
import Exceptions.DeclareException;
import Model.PrgState;
import Model.adt.*;
import Model.exp.Exp;
import Model.types.IType;
import Model.value.IValue;
import javafx.util.Pair;

import java.util.ArrayList;

public class CallProcedureStmt implements IStmt{
    private final String procedureName;
    private final ArrayList<Exp> argumentsValues;

    public CallProcedureStmt(String _proc, ArrayList<Exp> _argVal) {
        procedureName = _proc;
        argumentsValues = _argVal;
    }

    @Override
    public String toString() {
        return "call " + procedureName + "(" + argumentsValues.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IProcTable<String, Pair<ArrayList<String>, IStmt>> procTable = state.getProcTable();

        if (!procTable.isDefined(procedureName)) {
            throw new DeclareException("Procedure is not defined");
        }

        ArrayList<String> procedureVariables = procTable.lookup(procedureName).getKey();
        IStmt body = procTable.lookup(procedureName).getValue();

        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        IStack<IStmt> stack = state.getStack();

        IDict<String, IValue> procedureSymTbl = new MyDict<>();

        for (int i = 0; i < procedureVariables.size(); i++) {
            IValue expVal = argumentsValues.get(i).eval(symTbl, heapTbl);
            procedureSymTbl.add(procedureVariables.get(i), expVal);
        }

        state.getSymTableStack().push(procedureSymTbl);
        stack.push(new ReturnStmt());
        stack.push(body);

        return null;
    }
}