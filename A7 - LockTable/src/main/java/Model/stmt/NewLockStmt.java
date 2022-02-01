package Model.stmt;
import Exceptions.TypeException;
import Exceptions.DeclareException;
import Model.PrgState;
import Model.adt.MyHeap;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyLockTable;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.IntType;
import Model.types.StringType;
import Model.value.IValue;
import Model.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLockStmt implements IStmt{
    private final String variableId;
    private static Lock lock = new ReentrantLock();

    public NewLockStmt(String _var) {
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
        return "newLock(" + variableId + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyLockTable<Integer, Integer> lockTable = (MyLockTable<Integer, Integer>) state.getLockTable();

        if (symTbl.isDefined(variableId)) {
            lock.lock();
            int lockTableIndex = lockTable.getCurrentFreeAddress();
            lockTable.findNextFreeAddress(); // - in order to find the new address for the next statement
            lockTable.add(lockTableIndex, -1);
            symTbl.update(variableId, new IntValue(lockTableIndex));
            lock.unlock();
        } else throw new DeclareException("Variable not defined");

        return null;
    }
}