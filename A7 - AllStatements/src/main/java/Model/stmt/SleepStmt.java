package Model.stmt;

import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.stmt.IStmt;
import Model.types.IType;

public class SleepStmt implements IStmt{
    private Integer number;

    public SleepStmt(Integer _num) {
        number = _num;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "sleep(" + number + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IStack<IStmt> stack = state.getStack();

        if (number > 0) {
            stack.push(new SleepStmt(number - 1));
        }
        return null;
    }

}