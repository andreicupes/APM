package Model.stmt;

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
import java.io.FileReader;
import java.io.IOException;


public class OpenRFileStmt implements IStmt{
    private Exp expression;

    public OpenRFileStmt(Exp e){
        this.expression=e;
    }

    @Override
    public String toString(){
        return "(open_file '"+this.expression+"')";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, BufferedReader> fileTable = state.getFileTable();
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer,IValue> heapTbl = (MyHeap<Integer,IValue>) state.getHeapTable();
        IValue condition = expression.eval(symTbl,heapTbl);

        if (condition.getType().equals(new StringType())) {
            StringValue sv = (StringValue) condition;

            if (!fileTable.isDefined(sv.getValue())) {

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(sv.getValue()));
                } catch (IOException e) {
                    throw new FileException(e.getMessage());
                }

                fileTable.add(sv.getValue(), reader);

            } else throw new FileException("File Descriptor exists.");
        } else throw new TypeException("The expression is not of type string.");

        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typeExp = this.expression.typeCheck(typeEnv);
        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else throw new TypeException("The expression is not of type string.");
    }
}
