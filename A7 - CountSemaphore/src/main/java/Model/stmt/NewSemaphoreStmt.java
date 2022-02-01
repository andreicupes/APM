package Model.stmt;

import Exceptions.DeclareException;
import Exceptions.ExpException;
import Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IStack;
import Model.adt.MyHeap;
import Model.adt.MySemaphoreTable;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewSemaphoreStmt implements IStmt{
    private final String variableId;
    private final Exp exp1;
    private static Lock lock = new ReentrantLock();

    public NewSemaphoreStmt(String _var, Exp _exp1) {
        variableId = _var;
        exp1 = _exp1;
    }

    @Override
    public String toString() {
        return "newSemaphore(" + variableId + "; " + exp1.toString() + ")";
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws Exception {
        if (!typeEnv.isDefined(variableId)) {
            throw new DeclareException("Variable is not defined.");
        }

        if (!typeEnv.lookup(variableId).equals(new IntType())){
            throw new TypeException("Variable type is not int");
        }
        if(!exp1.typeCheck(typeEnv).equals(new IntType())) {
            throw new ExpException("Expression type is not int");
        }

        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState state) throws Exception {
        IDict<String, IValue> symTbl = state.getSymTable();
        MyHeap<Integer, IValue> heapTbl = (MyHeap<Integer, IValue>) state.getHeapTable();
        MySemaphoreTable<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTbl =
                (MySemaphoreTable<Integer, Pair<Integer, ArrayList<Integer>>>) state.getSemaphoreTable();

        if (symTbl.isDefined(variableId)) {
            lock.lock();
            int semFreePos = semaphoreTbl.getCurrentFreeAddress();

            IntValue number1 = (IntValue) exp1.eval(symTbl, heapTbl);

            semaphoreTbl.add(semFreePos, new Pair<Integer, ArrayList<Integer>>(number1.getValue(), new ArrayList<Integer>()));
            symTbl.update(variableId, new IntValue(semFreePos));
            lock.unlock();
        } else throw new DeclareException("Variable is not defined");

        return null;
    }
}
