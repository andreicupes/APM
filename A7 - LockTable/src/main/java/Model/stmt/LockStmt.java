package Model.stmt;
import Exceptions.DeclareException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.MyHeap;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyLockTable;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStmt implements IStmt{
    private final String variableId;
    private static Lock lock = new ReentrantLock();

    public LockStmt(String _var) {
        variableId = _var;
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        if (!typeEnv.isDefined(variableId)) {
            throw new DeclareException("Variable " + variableId + " is not defined");
        }

        if (!typeEnv.lookup(variableId).equals(new IntType())) {
            throw new TypeException("Variable type is not int");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "lock(" + variableId + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyLockTable<Integer, Integer> lockTable = (MyLockTable<Integer, Integer>) state.getLockTable();
        IStack<IStmt> stack = state.getStack();

        // we check that variableId represents a valid index in the symTbl

        IntValue lockTableIndex = (IntValue)symTbl.lookup(variableId);
        if (lockTable.isDefined(lockTableIndex.getValue())) {
            lock.lock();
            if (lockTable.lookup(lockTableIndex.getValue()) == -1) {
                lockTable.update(lockTableIndex.getValue(), state.getId());
            } else {
                stack.push(new LockStmt(variableId));
            }
            lock.unlock();
        } else throw new DeclareException("The variable does not have a lock index.");

        return null;
    }
}