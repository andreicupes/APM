package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.*;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownStmt implements IStmt{
    private final String variableId;
    private static Lock lock = new ReentrantLock();

    public CountDownStmt(String _var) {
        variableId = _var;
    }

    @Override
    public String toString() {
        return "countDown(" + variableId + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        if (!typeEnv.isDefined(variableId)) {
            throw new DeclareException("Variable is not defined2");
        }

        if (!typeEnv.lookup(variableId).equals(new IntType())) {
            throw new TypeException("Variable is not of type int");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        IList<IValue> output = state.getOutput();
        MyLatchTable<Integer, Integer> latchTbl = (MyLatchTable<Integer, Integer>)state.getLatchTable();

        IntValue foundIndex = (IntValue)symTbl.lookup(variableId);

        if (latchTbl.isDefined(foundIndex.getValue())) {
            lock.lock();
            int latchVal = latchTbl.lookup(foundIndex.getValue());
            if (latchVal > 0) {
                latchTbl.update(foundIndex.getValue(), latchVal - 1);
            }
            output.add(new IntValue(state.getId()));
            lock.unlock();
        } else throw new DeclareException("Latch index is not defined");

        return null;
    }
}