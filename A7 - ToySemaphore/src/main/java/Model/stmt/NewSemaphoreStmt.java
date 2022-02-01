package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.ExpException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.Triplet;
import Model.adt.IStack;
import Model.adt.MyHeap;
import Model.adt.MySemaphoreTable;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;


import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewSemaphoreStmt implements IStmt{
    private final String variableId;
    private final Exp exp1, exp2;
    private static Lock lock = new ReentrantLock();

    public NewSemaphoreStmt(String _var, Exp _exp1, Exp _exp2) {
        variableId = _var;
        exp1 = _exp1;
        exp2 = _exp2;
    }

    @Override
    public String toString() {
        return "newToySemaphore(" + variableId + "; " + exp1.toString() + "; "
                + exp2.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {

        if (!typeEnv.isDefined(variableId)) {
            throw new DeclareException("Variable is not defined");
        }

        if (!typeEnv.lookup(variableId).equals(new IntType())) {
            throw new TypeException("Variable is not of type int");
        }

        if (!exp1.typeCheck(typeEnv).equals(new IntType()) || !exp2.typeCheck(typeEnv).equals(new IntType())) {
            throw new ExpException("Expressions type is not int");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        IStack<IStmt> stack = state.getStack();
        MySemaphoreTable<Integer, Triplet<Integer, ArrayList<Integer>, Integer>> toySemaphoreTbl =
                (MySemaphoreTable<Integer, Triplet<Integer, ArrayList<Integer>, Integer>>) state.getSemaphoreTable();

        if (symTbl.isDefined(variableId)) {
            IValue number1 = exp1.eval(symTbl, heapTbl);
            IValue number2 = exp2.eval(symTbl, heapTbl);

            if (number1.getType().equals(new IntType()) && number2.getType().equals(new IntType())) {
                lock.lock();
                int freePosition = toySemaphoreTbl.getCurrentFreeAddress();
                toySemaphoreTbl.add(freePosition, new Triplet<>(
                        ((IntValue)number1).getValue(), new ArrayList<>(), ((IntValue)number2).getValue()
                ));
                symTbl.update(variableId, new IntValue(freePosition));
                lock.unlock();
            } else throw new ExpException("Expression type is not int");
        } else throw new DeclareException("Variable is not defined");


        return null;
    }
}
