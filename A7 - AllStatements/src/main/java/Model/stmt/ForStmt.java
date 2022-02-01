package Model.stmt;

import Exceptions.TypeException;
import Model.PrgState;

import Model.adt.IDict;
import Model.adt.IStack;
import Model.exp.Exp;
import Model.exp.RelationalExp;
import Model.exp.VarExp;
import Model.types.IType;
import Model.types.IntType;


public class ForStmt implements IStmt{
    private Exp exp1;
    private Exp exp2;
    private Exp exp3;
    private IStmt stmt;

    public ForStmt(Exp _exp1, Exp _exp2, Exp _exp3, IStmt _stmt) {
        exp1 = _exp1;
        exp2 = _exp2;
        exp3 = _exp3;
        stmt = _stmt;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typ1, typ2, typ3;
        if(typeEnv.lookup("v") == null) typeEnv.add("v", new IntType());
        typ1 = exp1.typeCheck(typeEnv);
        if (typ1.equals(new IntType())) {
            typ2 = exp2.typeCheck(typeEnv);

            if (typ2.equals(new IntType())) {
                typ3 = exp3.typeCheck(typeEnv);

                if (typ3.equals(new IntType())) {
                    stmt.typeCheck(typeEnv.deepCopy());
                } else throw new TypeException("Invalid Type3!");

            } else throw new TypeException("Invalid Type2!");
        } else throw new TypeException("Invalid Type1!");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "for(v=" + exp1.toString() + "; v < " + exp2.toString() + "; v=" + exp3.toString()
                + ") " + stmt.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IStack<IStmt> stack = state.getStack();

        IStmt forStmt = new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new AssignStmt("v", exp1),
                            new WhileStmt(new RelationalExp(new VarExp("v"), exp2, "<"),
                                    new CompStmt(stmt, new AssignStmt("v", exp3)))
                    ));

        stack.push(forStmt);

        return null;
    }
}
