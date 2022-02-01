package Model;

import Exceptions.ADTException;
import Model.adt.*;
import Model.stmt.IStmt;
import Model.value.IValue;
import Model.value.StringValue;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class PrgState {

    private IStack<IStmt> exeStack;
    private IStack<IDict<String, IValue>> symTable;
    private IList<IValue> out;
    private IStmt originalProgram; //optional field, but good to have
    private IDict<String, BufferedReader> fileTable;
    private IHeap<Integer, IValue> heapTable;
    private IProcTable<String, Pair<ArrayList<String>, IStmt>> procTable;
    private static int threadCount = 0;
    private final int threadId;
    //private IDict<int,IValue>

    private synchronized static int getThreadId() {
        threadCount ++;
        return threadCount - 1;
    }

    public PrgState(IStack<IStmt> _exestack, IStack<IDict<String, IValue>> _symtbl, IList<IValue> _out, IStmt _originalProgram, IDict<String, BufferedReader> _fileTable, IHeap<Integer, IValue> _heap,IProcTable<String, Pair<ArrayList<String>, IStmt>> _procTable) {
        this.exeStack = _exestack;
        this.symTable = _symtbl;
        this.out = _out;
        this.originalProgram = _originalProgram;
        this.fileTable =_fileTable;
        this.heapTable = _heap;
        this.procTable=_procTable;
        threadId = getThreadId();
    }

    public IList<IValue> getOutput() {
        return this.out;
    }

    public IStack<IDict<String, IValue>> getSymTableStack() {return this.symTable;}

    public IStack<IStmt> getStack() {
        return this.exeStack;
    }

    public IDict<String, BufferedReader> getFileTable() {return this.fileTable;}

    public IHeap<Integer, IValue> getHeapTable(){return this.heapTable;}

    public IProcTable<String, Pair<ArrayList<String>, IStmt>> getProcTable(){return this.procTable;}

    public void setOutput(IList<IValue> output) {
        this.out = output;
    }

    public int getId() { return this.threadId; }

    public PrgState oneStep() throws Exception{
        if (this.exeStack.isEmpty()) throw new ADTException("PrgState is empty.");

        IStmt crtStmt = this.exeStack.pop();
        return crtStmt.execute(this);
    }

    public Boolean isNotCompleted() {
        return !this.exeStack.isEmpty();
    }


    public String toString() {
        return "Thread ID:\n" + threadId + "\nStack: " + exeStack.toString() + "\nSymbol Table: " + symTable.toString() + "\nOut: " + out.toString() + "\nHeap: " + heapTable.toString() + '\n';
    }

    public String toFile() {
        return "---------------------------------START----------------------------------------------\n"+"Thread ID:\n" + threadId + "\nExeStack: \n" + exeStack.toString() + "\n" + "SymTable: \n" + symTable.toFile() + "\n" +
                "Out: \n" + out.toFile() + "\n" + "FileTable: \n" + fileTable.toFile() + "\n" +
                "Heap: \n" + heapTable.toFile() + "ProcTable: \n" + procTable.toFile() +"\n---------------------------------END-----------------------------------\n\n\n\n";
    }

    public IDict<String, IValue> getSymTable() { return this.symTable.top();
    }
}