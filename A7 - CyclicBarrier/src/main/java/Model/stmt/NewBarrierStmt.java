package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.ExpException;
import Model.PrgState;

import Model.adt.IDict;
import Model.adt.MyBarrierTable;
import Model.adt.MyHeap;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewBarrierStmt implements IStmt{
    private final String variableId;
    private final Exp exp;
    private static Lock lock = new ReentrantLock();

    public NewBarrierStmt(String _var, Exp _exp) {
        variableId = _var;
        exp = _exp;
    }

    @Override
    public String toString() {
        return "newBarrier(" + variableId + "; " + exp.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        if (!typeEnv.isDefined(variableId)) {
            throw new DeclareException("Variable is not defined");
        }

        if (!typeEnv.lookup(variableId).equals(new IntType())) {
            throw new DeclareException("Variable is not defined");
        }

        if (!exp.typeCheck(typeEnv).equals(new IntType())) {
            throw new ExpException("Expression type is not int");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        MyBarrierTable<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable = (MyBarrierTable<Integer, Pair<Integer, ArrayList<Integer>>>) state.getBarrierTable();

        if (symTbl.isDefined(variableId)) {
            IValue val = exp.eval(symTbl, heapTbl);

            if (val.getType().equals(new IntType())) {

                lock.lock();
                int expVal = ((IntValue)val).getValue();
                int barrierIndex = barrierTable.getCurrentFreeAddress();
                barrierTable.add(barrierIndex, new Pair<Integer, ArrayList<Integer>>(expVal, new ArrayList<Integer>()));
                symTbl.update(variableId, new IntValue(barrierIndex));

                lock.unlock();
            } else throw new ExpException("Expression is not int");

        } else throw new DeclareException("Variable is not defined");

        return null;
    }
}