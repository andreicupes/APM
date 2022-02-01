package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.FileException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyHeap;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.StringType;
import Model.value.IValue;
import Model.value.StringValue;

import java.io.BufferedReader;

public class CloseRFileStmt implements IStmt{
    private final Exp exp;

    public CloseRFileStmt(Exp _exp) {
        exp = _exp;
    }

    @Override
    public String toString() {
        return "(close file '" + this.exp +"')";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {

        IDict<String, IValue> symTbl = state.getSymTable();
        IDict<String, BufferedReader>  fileTable = state.getFileTable();
        MyHeap<Integer,IValue> heapTbl = (MyHeap<Integer,IValue>) state.getHeapTable();
        IValue condition = exp.eval(symTbl,heapTbl);

        if (condition.getType().equals(new StringType())){
            StringValue sv = (StringValue) condition;

            if (fileTable.isDefined(sv.getValue())) {
                BufferedReader reader = fileTable.lookup(sv.getValue());
                reader.close();
                fileTable.remove(sv.getValue());

            } else throw new DeclareException("Unknown entry for file.");

        } else throw new TypeException("The type must be string.");

        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typeExp = exp.typeCheck(typeEnv);
        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else throw new TypeException("Invalid expression type.");
    }
}
