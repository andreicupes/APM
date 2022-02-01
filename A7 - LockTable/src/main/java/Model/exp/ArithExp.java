package Model.exp;

import Exceptions.DivisionException;
import Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.MyHeap;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;

public class ArithExp extends Exp {
    private char op;
    private Exp e1, e2;

    //constructor
    public ArithExp(char _op, Exp _e1, Exp _e2) {
        this.op = _op;
        this.e1 = _e1;
        this.e2 = _e2;
    }

    public IValue eval(IDict<String, IValue> symTable, MyHeap<Integer,IValue> heapTable) throws Exception {

        IValue v1 = e1.eval(symTable,heapTable);

        if (v1.getType().equals(new IntType())) {
            IValue v2 = e2.eval(symTable,heapTable);
            if (v2.getType().equals(new IntType())) {
                IntValue nr1 = (IntValue) v1;
                IntValue nr2 = (IntValue) v2;

                switch (op) {
                    case '+':
                        return new IntValue(nr1.getValue() + nr2.getValue());
                    case '-':
                        return new IntValue(nr1.getValue() - nr2.getValue());
                    case '*':
                        return new IntValue(nr1.getValue() * nr2.getValue());
                    case '/':
                        if (nr2.getValue() == 0) {
                            throw new DivisionException("Division by 0.\n");
                        }
                        return new IntValue(nr1.getValue() / nr2.getValue());
                }
            } else {
                throw new TypeException("Operand 2 is not an integer.\n");
            }
        } else {
            throw new TypeException("Operand 1 is not an integer.\n");
        }
        return new IntValue();
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws Exception {
        IType typ1, typ2;

        typ1 = e1.typeCheck(typeEnv);
        typ2 = e2.typeCheck(typeEnv);

        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else throw new TypeException("Operand 2 is not an integer.\n");
        } else throw new TypeException("Operand 2 is not an integer.\n");
    }


    public char getOp() {
        return this.op;
    }

    public Exp getFirst() {
        return this.e1;
    }

    public Exp getSecond() {
        return this.e2;
    }

    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }
}
