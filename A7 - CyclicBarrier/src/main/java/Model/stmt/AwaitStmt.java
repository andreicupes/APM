package Model.stmt;

import Exceptions.DeclareException;
import Model.PrgState;
import Model.adt.MyHeap;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyBarrierTable;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitStmt implements IStmt{
    private final String variableId;
    private static Lock lock = new ReentrantLock();

    public AwaitStmt(String _var) {
        variableId = _var;
    }

    @Override
    public String toString() {
        return "awaitBarrier(" + variableId + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        if (!typeEnv.isDefined(variableId)) {
            throw new DeclareException("Variable is not defined");
        }

        if (!typeEnv.lookup(variableId).equals(new IntType())) {
            throw new DeclareException("Variable is not defined");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        MyBarrierTable<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable = (MyBarrierTable<Integer, Pair<Integer, ArrayList<Integer>>>) state.getBarrierTable();
        IStack<IStmt> stack = state.getStack();

        if (symTbl.isDefined(variableId)) {
            IntValue barrierIndex = (IntValue)symTbl.lookup(variableId);
            if (barrierTable.isDefined(barrierIndex.getValue())) {
                lock.lock();
                Pair<Integer, ArrayList<Integer>> entry = barrierTable.lookup(barrierIndex.getValue());
                Integer N1 = entry.getKey();
                ArrayList<Integer> list = entry.getValue();
                Integer NL = list.size();

                if (N1 > NL) {
                    if (!entry.getValue().contains(state.getId())) {
                        list.add(state.getId());
                        barrierTable.update(barrierIndex.getValue(), new Pair<Integer, ArrayList<Integer>>(N1, list));
                    }
                    stack.push(this);
                }

                lock.unlock();
            } else throw new DeclareException("Barrier Index is not defined.");

        } else throw new DeclareException("Variable is not defined");

        return null;
    }
}