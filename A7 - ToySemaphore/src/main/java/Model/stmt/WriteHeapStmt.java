package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.HeapException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;

public class WriteHeapStmt implements IStmt{
    private final String variableName;
    private final Exp exp;

    public WriteHeapStmt(String _variableName, Exp _exp) {
        this.variableName = _variableName;
        this.exp = _exp;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer,IValue> heapTbl = (MyHeap<Integer,IValue>) state.getHeapTable();

        if (symTbl.isDefined(variableName)) {
            IValue val = symTbl.lookup(variableName);
            if (val.getType() instanceof RefType) {
                int address = ((RefValue)val).getAddr();
                if (heapTbl.isDefined(address)) {
                    IValue evalExp = exp.eval(symTbl, heapTbl);
                    IType locationType = ((RefValue) val).getLocType();
                    if (evalExp.getType().equals(locationType)) {
                        heapTbl.update(address, evalExp);
                    } else throw new HeapException("Invalid type for the variable name and the heap");
                } else throw new HeapException("Uninitialized address memory");
            }else throw new TypeException("The variable type must be RefType.");
        } else throw new DeclareException("Variable undefined");

        return null;
    }

    @Override
    public String toString() {
        return "wH(" + variableName + " = " + exp.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typeVar = typeEnv.lookup(variableName);
        IType typeExp = exp.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExp))) {
            return typeEnv;
        } else throw new TypeException("The sides have different types.");
    }
}
