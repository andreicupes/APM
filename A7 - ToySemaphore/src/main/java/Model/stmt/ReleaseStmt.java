package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.*;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleaseStmt implements IStmt{
    private final String variableId;
    private static Lock lock = new ReentrantLock();

    public ReleaseStmt(String _var) {
        variableId = _var;
    }

    @Override
    public String toString() {
        return "release(" + variableId + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        if (!typeEnv.isDefined(variableId)) {
            throw new DeclareException("Variable is not defined");
        }

        if (!typeEnv.lookup(variableId).equals(new IntType())) {
            throw new TypeException("Variable is not int");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        IStack<IStmt> stack = state.getStack();
        MySemaphoreTable<Integer, Triplet<Integer, ArrayList<Integer>, Integer>> semaphoreTbl =
                (MySemaphoreTable<Integer, Triplet<Integer, ArrayList<Integer>, Integer>>) state.getSemaphoreTable();

        if (symTbl.isDefined(variableId)) {
            IntValue indexValue = (IntValue)symTbl.lookup(variableId);
            if (semaphoreTbl.isDefined(indexValue.getValue())) {
                lock.lock();

                Triplet<Integer, ArrayList<Integer>, Integer> entry = semaphoreTbl.lookup(indexValue.getValue());
                ArrayList<Integer> list = entry.getValue1();
                if (list.contains(state.getId())) {
                    list.remove(Integer.valueOf(state.getId()));
                }


                lock.unlock();
            } else throw new DeclareException("Semaphore index not defined");
        } else throw new DeclareException("Variable is not defined");


        return null;
    }
}