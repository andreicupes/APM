package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyHeap;
import Model.exp.Exp;
import Model.types.IType;
import Model.value.IValue;


public class AssignStmt implements IStmt {

    private String id;
    private Exp expression;

    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.expression = exp;
    }

    @Override
    public String toString() {
        return this.id + "=" + this.expression.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IStack<IStmt> stk = state.getStack();
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer,IValue> heapTbl = (MyHeap<Integer,IValue>) state.getHeapTable();
        if (symTbl.isDefined(id)) {
            IValue val = expression.eval(symTbl,heapTbl);
            IType typId = (symTbl.lookup(id)).getType();
            if ((val.getType()).equals(typId)) symTbl.update(id, val);
            else
                throw new DeclareException("declared type of variable " + id + " and type of the assigned expression do not match");
        } else throw new DeclareException("the used variable " + id + " was not declared before");
        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typeVar = typeEnv.lookup(id);
        IType typeExp = expression.typeCheck(typeEnv);

        if (typeVar.equals(typeExp)){
            return typeEnv;
        } else throw new TypeException("When assigning the variables have different types.");
    }
}
