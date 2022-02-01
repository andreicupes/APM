package Model.exp;

import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.types.BoolType;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class LogicExp extends Exp {
    private final String op;
    private final Exp e1;
    private final Exp e2;

    public LogicExp(String _op, Exp _e1, Exp _e2) {
        this.op = _op;
        this.e1 = _e1;
        this.e2 = _e2;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, MyHeap<Integer,IValue> heapTable) throws Exception {
        IValue v1 = e1.eval(symTable,heapTable);

        if (v1.getType().equals(new BoolType())) {
            IValue v2 = e2.eval(symTable,heapTable);
            if (v2.getType().equals(new BoolType())) {

                BoolValue n1 = (BoolValue) v1;
                BoolValue n2 = (BoolValue) v2;

                switch (op) {
                    case "and":
                        return new BoolValue(n1.getValue() && n2.getValue());
                    case "or":
                        return new BoolValue(n1.getValue() || n2.getValue());
                }
            } else {
                throw new TypeException("Operand 2 is not boolean.\n");
            }

        } else {
            throw new TypeException("Operand 1 is not boolean.\n");
        }

        return null;
    }

    @Override
    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typ1, typ2;

        typ1 = e1.typeCheck(typeEnv);
        typ2 = e2.typeCheck(typeEnv);

        if (typ1.equals(new BoolType())){
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            } else throw new TypeException("Operand 2 is not boolean.\n");
        } else throw new TypeException("Operand 1 is not boolean.\n");
    }
}
