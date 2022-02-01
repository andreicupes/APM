package Model.stmt;


import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.MyHeap;
import Model.adt.MyList;
import Model.exp.Exp;
import Model.types.IType;
import Model.value.IValue;

public class PrintStmt implements IStmt {

    private Exp expression;

    public PrintStmt(Exp exp) {
        this.expression = exp;
    }

    @Override
    public String toString() {
        return "print(" + this.expression.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IList<IValue> output = state.getOutput();
        MyHeap<Integer,IValue> heapTbl = (MyHeap<Integer,IValue>) state.getHeapTable();
        output.add(expression.eval(state.getSymTable(),heapTbl));
        state.setOutput(output);
        return null;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        expression.typeCheck(typeEnv);
        return typeEnv;
    }
}
