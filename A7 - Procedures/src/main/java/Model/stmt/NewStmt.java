package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.*;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;

public class NewStmt implements IStmt{
    private final String variableName;
    private final Exp exp;

    public NewStmt(String _variableName, Exp _exp) {
        variableName = _variableName;
        exp = _exp;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer,IValue> heapTbl = (MyHeap<Integer,IValue>) state.getHeapTable();

        if (symTbl.isDefined(variableName)) {
            IValue val = symTbl.lookup(variableName);
            if (val.getType() instanceof RefType) {
                IValue cond = exp.eval(symTbl, heapTbl);
                RefValue refVal = (RefValue) val;
                if (cond.getType().equals(refVal.getLocType())) {
                    int pos = heapTbl.add(cond);
                    RefValue copyRef = (RefValue) refVal.deepCopy();
                    copyRef.setAddr(pos);
                    symTbl.update(variableName, copyRef);

                } else throw new TypeException("The types of the variables are different.");
            } else throw new TypeException("Variable type must be RefType!");
        } else throw new DeclareException("Variable undefined!");

        return null;
    }

    @Override
    public String toString() {
        return "(new " + variableName + " " + exp.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typeVar = typeEnv.lookup(variableName);
        IType typeExp = exp.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExp))) {
            return typeEnv;
        } else throw new TypeException("The types of the variables are different.");
    }

}