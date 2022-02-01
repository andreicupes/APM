package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.FileException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.IntType;
import Model.types.StringType;
import Model.value.IValue;
import Model.value.IntValue;
import Model.value.StringValue;

import java.io.BufferedReader;

public class ReadFileStmt implements IStmt{
    private final String var_name;
    private final Exp exp;

    public ReadFileStmt(Exp _exp, String _var_name) {
        var_name = _var_name;
        exp = _exp;
    }

    @Override
    public String toString() {
        return "(read_file '" + this.exp +"')";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {

        IDict<String, IValue> symTbl = state.getSymTable();
        IDict<String, BufferedReader>  fileTable = state.getFileTable();
        MyHeap<Integer,IValue> heapTbl = (MyHeap<Integer,IValue>) state.getHeapTable();
        if (symTbl.isDefined(var_name)) {
            IValue v = symTbl.lookup(var_name);

            if (v.getType().equals(new IntType())) {
                IValue condition = exp.eval(symTbl,heapTbl);

                if (condition.getType().equals(new StringType())) {

                    StringValue sv = (StringValue) condition;

                    if (fileTable.isDefined(sv.getValue())) {
                        BufferedReader reader = fileTable.lookup(sv.getValue());
                        String line = reader.readLine();

                        if (line == null) {
                            symTbl.update(var_name, new IntValue(0));
                        } else {
                            symTbl.update(var_name, new IntValue(Integer.parseInt(line)));
                        }

                    } else throw new FileException("File not defined.");

                } else throw new TypeException("Variable type is not string.");

            } else throw new TypeException("Variable type is not int");

        } else throw new DeclareException("Variable undefined.");

        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typeVar = typeEnv.lookup(var_name);
        IType typeExp = exp.typeCheck(typeEnv);
        if (typeVar.equals(new IntType()) && typeExp.equals(new StringType())) {
            return typeEnv;
        } else throw new TypeException("Invalid variable name or expression type.");
    }
}
