package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyLockTable;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnlockStmt implements IStmt{
    private final String variableId;
    private static Lock lock;

    public UnlockStmt(String _var) {
        variableId = _var;
        lock = new ReentrantLock();
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        if (!typeEnv.isDefined(variableId)) {
            throw new DeclareException("Variable is undefined");
        }

        if (!typeEnv.lookup(variableId).equals(new IntType())) {
            throw new TypeException("Variable type is not int");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "unlock(" + variableId + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyLockTable<Integer, Integer> lockTable = (MyLockTable<Integer, Integer>) state.getLockTable();
        IStack<IStmt> stack = state.getStack();

        Integer lockTableIndex = ((IntValue)symTbl.lookup(variableId)).getValue();

        if (lockTable.isDefined(lockTableIndex)) {
            lockTable.update(lockTableIndex, -1);
        } else throw new DeclareException("The variable is not defined in the lock table");

        return null;
    }
}